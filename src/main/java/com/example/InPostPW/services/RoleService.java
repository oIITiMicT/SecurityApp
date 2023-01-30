package com.example.InPostPW.services;

import java.util.Optional;
import com.example.InPostPW.model.Role;

public interface RoleService {
    Optional<Role> findRoleByName(String name);

    void saveRole(Role role);
}
