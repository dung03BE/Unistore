package com.dung.UniStore.utils;



import com.dung.UniStore.dto.request.IntroSpectRequest;
import com.dung.UniStore.dto.response.IntroSpectResponse;
import com.dung.UniStore.entity.User;
import com.dung.UniStore.exception.AppException;
import com.dung.UniStore.exception.ErrorCode;
import com.dung.UniStore.repository.ITokenRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;

import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;


import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
@RequiredArgsConstructor

public class JwtTokenUtil {
    private final ITokenRepository tokenRepository;
//    @NonFinal
//    @Value("${jwt.signerKey}")
//    protected   String SIGNER_KEY ;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;

    public String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getPhoneNumber())
                .issuer("dung.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        //kys token
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            e.printStackTrace();
        }
        return null;
    }
    public IntroSpectResponse introspect(IntroSpectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();
        boolean isValid=true;
        try {
            verifyToken(token,false);
        }
        catch(AppException e)
        {
            isValid=false;
        }
        return  IntroSpectResponse.builder()
                .valid(isValid)
                .build();

    }
    public SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT=SignedJWT.parse(token);
        Date expityTime = (isRefresh)? new Date (signedJWT.getJWTClaimsSet().getIssueTime().toInstant().plus(REFRESHABLE_DURATION,ChronoUnit.SECONDS).toEpochMilli())
                :signedJWT.getJWTClaimsSet().getExpirationTime();
        var verified =  signedJWT.verify(verifier);
       if(!(verified && expityTime.after(new Date())))
       {
           throw  new AppException(ErrorCode.UNAUTHENTICATED);
       }
       if(tokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
           throw  new AppException(ErrorCode.UNAUTHENTICATED);

        return signedJWT;
    }
    private String buildScope(User user) {
        //cách Set<Role>
//        StringJoiner stringJoiner = new StringJoiner(" ");
//        if(!CollectionUtils.isEmpty((Collection<?>) user.getRole()))
//        {
//            ((Collection<?>) user.getRole()).forEach(s->stringJoiner.add((CharSequence) s));
//
//        }
//        return stringJoiner.toString();
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (user.getRole() != null) {
            stringJoiner.add("ROLE_"+user.getRole().getName().toUpperCase());

            // Thêm các permissions từ role vào scope
            if (!CollectionUtils.isEmpty(user.getRole().getPermissions())) {
                user.getRole().getPermissions().forEach(permission -> {
                    stringJoiner.add(permission.getName().toUpperCase());
                });
            }
        }

        // Nếu không có role và permission, trả về giá trị mặc định
        return stringJoiner.length() > 0 ? stringJoiner.toString() : "DEFAULT_ROLE";

    }


}
