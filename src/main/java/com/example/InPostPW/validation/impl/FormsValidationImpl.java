package com.example.InPostPW.validation.impl;

import com.example.InPostPW.dto.RegistrationFormDto;
import com.example.InPostPW.exception.IllegalFormFieldException;
import com.example.InPostPW.services.UserService;
import com.example.InPostPW.validation.FormsValidation;
import com.example.InPostPW.validation.PasswordValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class FormsValidationImpl implements FormsValidation {

    private static final String DUPLICATE_USERNAME_MESSAGE = "user with this username exist";
    private static final String DUPLICATE_EMAIL_MESSAGE = "user with this email exist";

    private final PasswordValidation passwordValidation;

    private final UserService userService;

    @Override
    public void validateRegistrationForm(RegistrationFormDto registrationForm) {
        final String email = registrationForm.getEmail();
        final String password = registrationForm.getPassword();

        if (email == null) {
            throw new IllegalFormFieldException("email");
        }

        if (password == null) {
            throw new IllegalFormFieldException("password");
        }

        if (userService.findUserByEmail(email).isPresent()) {
            throw new IllegalFormFieldException(DUPLICATE_EMAIL_MESSAGE, email);
        }

        passwordValidation.checkPasswordValidity(registrationForm.getPassword());
    }

}
