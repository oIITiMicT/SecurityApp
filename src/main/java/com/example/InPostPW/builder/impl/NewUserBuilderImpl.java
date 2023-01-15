package com.example.InPostPW.builder.impl;

import com.example.InPostPW.builder.NewUserBuilder;
import com.example.InPostPW.dto.RegistrationFormDto;
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


    private String generateSalt() {
        byte[] array = new byte[5];
        Random random = new Random(System.nanoTime());
        random.nextBytes(array);
        return new String(array, StandardCharsets.UTF_8);
    }

    //TODO builder
    @Override
    public User createNewUser(RegistrationFormDto registrationForm) {
        formsValidation.validateRegistrationForm(registrationForm);
        User user = new User();
        String salt = generateSalt();
        String securityPassword = salt + registrationForm.getPassword();
        user.setPassword(passwordEncoder.encode(securityPassword));
        user.setEmail(registrationForm.getEmail());
        user.setRole(roleService.findRoleByName("user").get());
        user.setNotes(new ArrayList<>());
        user.setSalt(salt);
        return user;
    }
}
