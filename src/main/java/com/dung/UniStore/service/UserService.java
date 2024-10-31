package com.dung.UniStore.service;

import com.dung.UniStore.dto.response.UserResponse;
import com.dung.UniStore.entity.Role;
import com.dung.UniStore.entity.User;
import com.dung.UniStore.dto.request.UserCreationRequest;
import com.dung.UniStore.exception.AppException;
import com.dung.UniStore.exception.ErrorCode;
import com.dung.UniStore.mapper.UserMapper;
import com.dung.UniStore.repository.IRoleRepository;
import com.dung.UniStore.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    public UserResponse createUser(UserCreationRequest form) throws Exception {

        String phoneNumber = form.getPhoneNumber();
        // Kiểm tra số điện thoại đã tồn tại chưa
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        // Kiểm tra Role tồn tại không
        Role role = roleRepository.findById(form.getRoleId()).orElseThrow(() -> new Exception("Role not found"));

        if (role.getName().toUpperCase().equals(Role.ADMIN)) {
            // Không được phép
            throw new Exception("Admin role cannot be assigned");
        }

        // Sử dụng MapStruct để ánh xạ DTO sang Entity
        log.debug("UserCreationRequest before mapping: {}", form);
        User user = userMapper.toUser(form);
        log.info("Mapped User: {}", user);
        user.setRole(role);

        // Kiểm tra nếu không có accountId, bắt buộc phải có password
        if (form.getFacebookAccountId() == 0 && form.getGoogleAccountId() == 0) {
            String password = form.getPassword();
            String encodedPassword = passwordEncoder.encode(password); // Mã hóa mật khẩu
            user.setPassword(encodedPassword);
        }

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        // Trả về phản hồi sử dụng mapper
        return userMapper.toUserResponse(user);
    }


    @PreAuthorize("hasRole('ADMIN')") // ktra admin trc khi vaof method
    //@PostAuthorize("hasRole('ADMIN')") //goi method r moi kiem tra admin
    /// sẽ k hoạt động vì hasRole chỉ hd với Role k permission nen phải sd hasAuthority @PreAuthorize("hasAuthority('APPROVE_POST')")
    public List<User> getAllUsers() {
        log.info("In method get info");
        return userRepository.findAll();
    }
//post hoox tro them laays đúng user dang login
    @PostAuthorize("returnObject.phoneNumber==authentication.name")
    @Cacheable(value = "users", key = "#id")
    public User getUserById(int id) {
        log.info("In method get info");
        return userRepository.findById(id).get();
    }
    public User getMyInfo() throws Exception {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user= userRepository.findByPhoneNumber(name).orElseThrow(()-> new Exception("K co user"));
            return user;
    }
}
