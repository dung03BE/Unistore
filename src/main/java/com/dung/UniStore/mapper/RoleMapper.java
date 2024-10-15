package com.dung.UniStore.mapper;


import com.dung.UniStore.dto.request.RoleRequest;
import com.dung.UniStore.dto.response.RoleResponse;
import com.dung.UniStore.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);

}