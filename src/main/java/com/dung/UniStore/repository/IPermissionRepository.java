package com.dung.UniStore.repository;


import com.dung.UniStore.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface IPermissionRepository extends JpaRepository<Permission, UUID> {
    void deleteByName(String name);

    List<Permission> findByNameIn(Set<String> names);
}
