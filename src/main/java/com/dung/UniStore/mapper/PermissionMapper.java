package com.dung.UniStore.mapper;



import com.dung.UniStore.dto.request.PermissionRequest;
import com.dung.UniStore.dto.response.PermissionResponse;
import com.dung.UniStore.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
