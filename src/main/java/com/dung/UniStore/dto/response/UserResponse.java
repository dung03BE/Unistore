package com.dung.UniStore.dto.response;

import com.dung.UniStore.entity.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String phoneNumber;
    String fullName;
    String address;
    Date dateOfBirth;
    private int roleId;
}