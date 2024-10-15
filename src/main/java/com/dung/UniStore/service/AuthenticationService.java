package com.dung.UniStore.service;


import com.dung.UniStore.dto.request.AuthenticationRequest;
import com.dung.UniStore.dto.request.LogoutRequest;
import com.dung.UniStore.dto.request.RefreshRequest;
import com.dung.UniStore.dto.response.AuthenticationResponse;
import com.dung.UniStore.entity.Token;
import com.dung.UniStore.entity.User;
import com.dung.UniStore.exception.AppException;
import com.dung.UniStore.exception.ErrorCode;
import com.dung.UniStore.repository.ITokenRepository;
import com.dung.UniStore.repository.IUserRepository;
import com.dung.UniStore.utils.JwtTokenUtil;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final ITokenRepository tokenRepository;

    public AuthenticationResponse authencicate(AuthenticationRequest request)
    {
        Optional<User> optionalUser = userRepository.findByPhoneNumber(request.getPhoneNumber());

        // Kiểm tra xem người dùng có tồn tại hay không
        if (optionalUser.isEmpty()) {
            // Trả về phản hồi lỗi nếu không tìm thấy người dùng
            return AuthenticationResponse.builder()
                    .authenticated(false)

                    .build();
        }
        //check password
//        if(existingUser.getFacebookAccountId()==0 && existingUser.getGoogleAccountId()==0)
//        {
//            if(!passwordEncoder.matches(request.getPassword(),existingUser.getPassword()))
//            {
//                throw new BadCredentialsException("Wrong phone number or password!");
//            }
//        }
        User existingUser = optionalUser.get();
        boolean authenticated = passwordEncoder.matches(request.getPassword(), existingUser.getPassword());
        if(!authenticated)
        {
            return null;
        }
        var token = jwtTokenUtil.generateToken(existingUser);
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    public void logout(LogoutRequest request) {
        try {
            var signToken =jwtTokenUtil.verifyToken(request.getToken(),true);

            String jit = signToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

            Token invalidatedToken =
                    Token.builder().id(jit).expiryTime(expiryTime).build();

            tokenRepository.save(invalidatedToken);
        } catch (AppException | JOSEException | ParseException exception) {
            log.info("Token already expired");
        }
    }
    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        var signJWT = jwtTokenUtil.verifyToken(request.getToken(),true);
        var jit = signJWT.getJWTClaimsSet().getJWTID();
        var expiryTime = signJWT.getJWTClaimsSet().getExpirationTime();
        Token invalidatedToken =
                Token.builder().id(jit).expiryTime(expiryTime).build();

        tokenRepository.save(invalidatedToken);
        var phonenumber =signJWT.getJWTClaimsSet().getSubject();
        var user =userRepository.findByPhoneNumber(phonenumber)
                .orElseThrow(()->new AppException(ErrorCode.UNAUTHENTICATED));
        var token = jwtTokenUtil.generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }
}
