package com.example.InPostPW.services.impl;

import java.util.Optional;
import org.springframework.stereotype.Service;
import com.example.InPostPW.repository.RoleRepository;
import com.example.InPostPW.services.RoleService;
import com.example.InPostPW.model.Role;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Optional<Role> findRoleByName(String name) {
        return roleRepository.findRoleByName(name);
    }

    @Override
    public void saveRole(Role role) {
        roleRepository.save(role);
    }
}
