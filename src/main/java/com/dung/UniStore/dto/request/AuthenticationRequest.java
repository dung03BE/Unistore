package com.dung.UniStore.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Data
public class AuthenticationRequest {
    private String phoneNumber;
    private String password;
}
