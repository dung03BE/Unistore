package com.dung.UniStore.controller;


import com.dung.UniStore.dto.request.UserCreationRequest;
import com.dung.UniStore.dto.response.UserResponse;
import com.dung.UniStore.exception.GlobalExceptionHandler;
import com.dung.UniStore.service.UserService;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
public class UserControllerTest {

    private MockMvc mockMvc;
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }


    @MockBean
    private UserService userService;
    private UserCreationRequest request;
    private UserResponse userResponse;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private long dateOfBirth;

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
    }
    @Test
    void createUser_validRequest_success() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(request);

        Mockito.when(userService.createUser(ArgumentMatchers.any())).thenReturn(userResponse);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
                .andExpect(MockMvcResultMatchers.jsonPath("result.id").value("cf0600f538b3"))
                .andExpect(MockMvcResultMatchers.jsonPath("result.phoneNumber").value("0343016013"));
    }

    @Test
        //
    void createUser_usernameInvalid_fail() throws Exception {
        // GIVEN
        request.setPhoneNumber("ahd");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(request);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("code")
                        .value(1003))
                .andExpect(MockMvcResultMatchers.jsonPath("message")
                        .value("Username must be at least 4 characters")
                );

    }
}

//cach 2
//import java.time.LocalDate;
//
//import com.dung.jwt.dto.request.UserCreationRequest;
//import com.dung.jwt.dto.response.UserResponse;
//import com.dung.jwt.service.UserService;
//import com.fasterxml.jackson.annotation.JsonFormat;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentMatchers;
//import org.mockito.Mockito;
//
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.util.Date;
//@Slf4j
//@SpringBootTest
//@AutoConfigureMockMvc
//public class UserControllerTest {
//
//    private MockMvc mockMvc;
//
//
//    @BeforeEach
//    void setUp() {
//        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userService)).build();
//    }
//    @MockBean
//    private UserService userService;
//    private UserCreationRequest request;
//    private UserResponse userResponse;
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
//    private long dateOfBirth;
//
//    @BeforeEach
//    void initData() {
//        // Set thông tin UserCreationRequest, nhưng dateOfBirth sẽ được set dưới dạng chuỗi trong JSON
//        request = UserCreationRequest.builder()
//                .fullName("John Doe")
//                .phoneNumber("0343016013")
//                .address("Hà Nội")
//                .password("password123")
//                .retypePassword("password123")
//                .facebookAccountId(0)
//                .googleAccountId(0)
//                .roleId(3)
//                // Không set dateOfBirth trực tiếp ở đây
//                .build();
//
//        userResponse = UserResponse.builder()
//                .id("cf0600f538b3")
//                .phoneNumber("0343016013")
//                .address("Hà Nội")
//                .dateOfBirth(new Date())  // Set giá trị Date
//                .roleId(3)
//                .fullName("John Doe")
//                .build();
//    }
//    @Test
//    void createUser_validRequest_success() throws Exception {
//        // GIVEN
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule()); // Đăng ký module cho Date/LocalDate nếu cần
//
//        // Truyền dateOfBirth dưới dạng chuỗi
//        String jsonRequest = """
//                    {
//                         "phone_number":"0343016013",
//                                                "address":"Hà Nội",
//                                                "password":"0343016013",
//                                                "retype_password":"0343016013",
//                                                "date_of_birth":"2003-01-01",
//                                                "facebook_account_id":0,
//                                                "google_account_id":0,
//                                                "role_id":3
//                    }
//                """;
//
//        Mockito.when(userService.createUser(ArgumentMatchers.any())).thenReturn(userResponse);
//
//        // WHEN, THEN
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/register")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content(jsonRequest))  // Truyền chuỗi JSON
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
//                .andExpect(MockMvcResultMatchers.jsonPath("result.id").value("cf0600f538b3"));
//    }
//}

