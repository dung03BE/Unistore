package com.dung.UniStore.service;

import com.dung.UniStore.dto.request.RoleRequest;
import com.dung.UniStore.dto.response.RoleResponse;
import com.dung.UniStore.entity.Permission;
import com.dung.UniStore.mapper.RoleMapper;
import com.dung.UniStore.repository.IPermissionRepository;
import com.dung.UniStore.repository.IRoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    IRoleRepository roleRepository;
    IPermissionRepository permissionRepository;
    RoleMapper roleMapper;

    public RoleResponse create(RoleRequest request) {
        var role = roleMapper.toRole(request);

        if (role.getName() == null || role.getName().isEmpty()) {
            throw new IllegalArgumentException("Role name cannot be null or empty");
        }
        var permissions = permissionRepository.findByNameIn(request.getPermissions());

        role.setPermissions(new HashSet<>(permissions));

        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    public List<RoleResponse> getAll() {
        return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).toList();
    }


}
