package com.dung.UniStore.dto.request;

import com.dung.UniStore.validator.DobConstraint;
import com.dung.UniStore.validator.DobConstraint;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    String password;
    String fullName;

    @DobConstraint(min = 18, message = "INVALID_DOB")
    Date dateOfBirth;


    int roleId;
}