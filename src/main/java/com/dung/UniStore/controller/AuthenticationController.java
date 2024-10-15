package com.dung.UniStore.controller;


import com.dung.UniStore.dto.request.AuthenticationRequest;
import com.dung.UniStore.dto.request.IntroSpectRequest;
import com.dung.UniStore.dto.request.LogoutRequest;
import com.dung.UniStore.dto.request.RefreshRequest;
import com.dung.UniStore.dto.response.ApiResponse;
import com.dung.UniStore.dto.response.AuthenticationResponse;
import com.dung.UniStore.dto.response.IntroSpectResponse;
import com.dung.UniStore.service.AuthenticationService;
import com.dung.UniStore.utils.JwtTokenUtil;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final JwtTokenUtil jwtTokenUtil;
    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> loginAccount(@RequestBody AuthenticationRequest request) {
       var result = authenticationService.authencicate(request);
       return ApiResponse.<AuthenticationResponse>builder()
               .result(result)
               .build();
    }
    @PostMapping("/refresh")
    public ApiResponse<AuthenticationResponse> refreshToken (@RequestBody RefreshRequest request) throws ParseException, JOSEException {
        var result = authenticationService.refreshToken(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }
    @PostMapping("/introspect")
    public ApiResponse<IntroSpectResponse> authenticate(@RequestBody IntroSpectRequest request) throws ParseException, JOSEException {
        var result = jwtTokenUtil.introspect(request);
        return ApiResponse.<IntroSpectResponse>builder()
                .result(result)
                .build();
    }
    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authenticationService.logout(request);
        return ApiResponse.<Void>builder().build();
    }
}
