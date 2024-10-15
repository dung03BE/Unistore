package com.dung.UniStore.mapper;


import com.dung.UniStore.dto.request.UserCreationRequest;
import com.dung.UniStore.dto.request.UserUpdateRequest;
import com.dung.UniStore.dto.response.UserResponse;
import com.dung.UniStore.entity.User;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "role.id", source = "roleId")
    User toUser(UserCreationRequest request);


    @Mapping(target = "roleId", source = "role.id")
    UserResponse toUserResponse(User user);


    void updateUser(@MappingTarget User user, UserUpdateRequest request);


}