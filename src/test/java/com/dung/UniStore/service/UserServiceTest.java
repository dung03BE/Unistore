package com.dung.UniStore.service;


import com.dung.UniStore.dto.request.UserCreationRequest;
import com.dung.UniStore.dto.response.UserResponse;
import com.dung.UniStore.entity.Role;
import com.dung.UniStore.entity.User;
import com.dung.UniStore.exception.AppException;
import com.dung.UniStore.repository.IRoleRepository;
import com.dung.UniStore.repository.IUserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest

@TestPropertySource("/test.properties")
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private IUserRepository userRepository;
    @MockBean
    private IRoleRepository roleRepository;
    private UserCreationRequest request;
    private UserResponse userResponse;
    private User user;
    private Date dateOfBirth;
    private Role role;
    @BeforeEach
    void initData() throws ParseException {
        String dateOfBirthString = "2003-03-03";

        // Chuyển đổi chuỗi ngày sinh thành Date
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date dateOfBirth = formatter.parse(dateOfBirthString);
        request = UserCreationRequest.builder()
                .phoneNumber("0343016013")
                .address("Hà Nội")
                .password("0343016013")
                .retypePassword("0343016013")
                .facebookAccountId(0)
                .googleAccountId(0)
                .dateOfBirth(dateOfBirth)
                .roleId(3)
                .build();

        userResponse = UserResponse.builder()
                .id("cf0600f538b3")
                .phoneNumber("0343016013")
                .address("Hà Nội")
                .dateOfBirth(dateOfBirth)
                .roleId(3)
                .fullName("")
                .build();
        role = Role.builder()
                .id(3)
                .name("USER")
                .build();
        user = User.builder()
                .id(1)
                .phoneNumber("0343016013")
                .address("Hà Nội")
                .role(role)
                .dateOfBirth(dateOfBirth)

                .fullName("")
                .build();
    }

    @Test
    void  reateUser_validRequest_success() throws Exception {
        // GIVEN
        when(userRepository.existsByPhoneNumber(anyString())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(user);
        when(roleRepository.findById(anyInt())).thenReturn(Optional.of(role));

        // WHEN
        var response = userService.createUser(request);
        // THEN

        Assertions.assertThat(response.getId()).isEqualTo("1");
        Assertions.assertThat(response.getPhoneNumber()).isEqualTo("0343016013");
    }

    @Test
    void createUser_userExisted_fail(){
        // GIVEN
        when(userRepository.existsByPhoneNumber(anyString())).thenReturn(true);

        // WHEN
        var exception = assertThrows(AppException.class,
                () -> userService.createUser(request));

        // THEN
        Assertions.assertThat(exception.getErrorCode().getCode())
                .isEqualTo(1002);
    }
}