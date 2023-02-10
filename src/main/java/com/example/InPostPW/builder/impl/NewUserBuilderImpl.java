package com.example.InPostPW.builder.impl;

import com.example.InPostPW.builder.NewUserBuilder;
import com.example.InPostPW.dto.RegistrationFormDto;
import com.example.InPostPW.model.Role;
import com.example.InPostPW.model.User;
import com.example.InPostPW.services.RoleService;
import com.example.InPostPW.validation.FormsValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;

@RequiredArgsConstructor
@Component
public class NewUserBuilderImpl implements NewUserBuilder {

    private final PasswordEncoder passwordEncoder;

    private final RoleService roleService;

    private final FormsValidation formsValidation;


    @Override
    public User createNewUser(RegistrationFormDto registrationForm) {
        if (roleService.findRoleByName("user").isEmpty()) {
            Role role = new Role();
            role.setName("user");
            roleService.saveRole(role);
        }
        formsValidation.validateRegistrationForm(registrationForm);
        User user = new User();
        user.setPassword(passwordEncoder.encode(registrationForm.getPassword()));
        user.setEmail(registrationForm.getEmail());
        user.setRole(roleService.findRoleByName("user").get());
        user.setNotes(new ArrayList<>());
        user.setStatus(1);
        user.setAttempts(0);
        user.setEmailBlock(1);
        return user;
    }
}
