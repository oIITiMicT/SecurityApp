package com.example.InPostPW.builder;

import com.example.InPostPW.dto.RegistrationFormDto;
import com.example.InPostPW.model.User;

public interface NewUserBuilder {
    User createNewUser(RegistrationFormDto registrationForm);
}
