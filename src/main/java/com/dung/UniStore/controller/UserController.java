package com.dung.UniStore.controller;


import com.dung.UniStore.dto.request.UserCreationRequest;
import com.dung.UniStore.dto.response.ApiResponse;
import com.dung.UniStore.dto.response.UserResponse;
import com.dung.UniStore.entity.User;
import com.dung.UniStore.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("api/v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> getAllUsers()
    {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        List<User> users = userService.getAllUsers();
        log.info("Username: {}",authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));
        return  users;
    }
    @GetMapping("{id}")
    public User getUserById(@PathVariable int id)
    {
        User users = (User) userService.getUserById(id);
        return  users;
    }
    @GetMapping("/myInfo")
    public User getMyInfo() throws Exception {
        User users = (User) userService.getMyInfo();
        return  users;
    }
    @PostMapping("/register")
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) throws Exception {
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(request))
                .build();
    }

}
