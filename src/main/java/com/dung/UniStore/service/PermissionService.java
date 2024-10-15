package com.dung.UniStore.service;

import com.dung.UniStore.dto.request.PermissionRequest;
import com.dung.UniStore.dto.response.PermissionResponse;
import com.dung.UniStore.entity.Permission;
import com.dung.UniStore.mapper.PermissionMapper;
import com.dung.UniStore.repository.IPermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class PermissionService {
    IPermissionRepository permissionRepository;
    PermissionMapper permissionMapper;
    public PermissionResponse create(PermissionRequest request)
    {
        Permission permission = permissionMapper.toPermission(request);
        Permission permissionNew = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permissionNew);
    }
    public List<PermissionResponse> getAll() {
        var permissions = permissionRepository.findAll();
        return permissions.stream().map(permissionMapper::toPermissionResponse).toList();
    }
    public void delete(String name) {
        permissionRepository.deleteByName(name);
    }
}
