package com.example.InPostPW.controller;

import com.example.InPostPW.builder.NewUserBuilder;
import com.example.InPostPW.dto.RegistrationFormDto;
import com.example.InPostPW.model.User;
import com.example.InPostPW.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private static final String NO_USER_MESSAGE = "User not found";
    private final UserService userService;
    private final NewUserBuilder userBuilder;

    @PostMapping("/registration")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationFormDto registrationForm) throws InterruptedException {
        User user = userBuilder.createNewUser(registrationForm);
        userService.saveUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
