package com.example.InPostPW.services;

import com.example.InPostPW.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService extends UserDetailsService {

    Optional<User> findUserByEmail(String email);

    User saveUser(User user);
}
