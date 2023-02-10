package com.example.InPostPW.filter;


import com.example.InPostPW.model.User;
import com.example.InPostPW.services.EmailSender;
import com.example.InPostPW.services.UserService;
import com.example.InPostPW.services.UserTokenProvider;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;


@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final String ACCESS_TOKEN = "accessToken";
    private static final String REFRESH_TOKEN = "refreshToken";

    private final AuthenticationManager authenticationManager;

    private final UserService userService;
    private final EmailSender emailSender;
    private final PasswordEncoder passwordEncoder;
    private final UserTokenProvider userTokenProvider;

    private String generateUnblockCode(User user) {
        SecureRandom secureRandom = new SecureRandom();
        int leftLimit = 48;
        int rightLimit = 122;
        int length = 8;
        return secureRandom.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    private void validateAttempts(User user) {
        if (user.getEmailBlock() == 0) return;
        int attempts = user.getAttempts() + 1;
        if (attempts < 4) {
            user.setAttempts(attempts);
            userService.saveUser(user);
            return;
        }
        user.setAttempts(0);
        if (user.getStatus() == 1) {
            user.setStatus(0);
        }
        String code = generateUnblockCode(user);
        emailSender.sendEmail(user.getEmail(), "unblock code", code);
        user.setUnblockCode(passwordEncoder.encode(code));
        userService.saveUser(user);
    }

    private void checkStatus(User user, String code) {
        if (user.getStatus() == 0) {
            String codeHash = user.getUnblockCode();
            if (passwordEncoder.matches(code, codeHash)) {
                user.setStatus(1);
                user.setUnblockCode(null);
                userService.saveUser(user);
            }
        }
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //TODO
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String username = null;
        String password;
        Authentication authentication = null;

        try {
            JsonNode json = new ObjectMapper().readTree(request.getReader());
            if (!(json.hasNonNull("email") && json.hasNonNull("password"))) {
                throw new AuthenticationServiceException("Invalid request. Expected JSON with 'email' and 'password' fields.");
            }
            username = json.get("email").asText();
            password = json.get("password").asText();
            String code = json.get("code").asText();
            Optional<User> user = userService.findUserByEmail(username);
            user.ifPresent(value -> checkStatus(value, code));
            user.ifPresent(this::validateAttempts);
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(username, password);
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (AuthenticationException e) {
            throw new AuthenticationServiceException(String.format("Error during authentication of %s", username), e);
        } catch (IOException e) {
            throw new AuthenticationServiceException("Error reading request body by ObjectMapper", e);
        }
        return authentication;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        //TODO
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        UserDetails user = (UserDetails)authResult.getPrincipal();
        String username = user.getUsername();
        User userModel = userService.findUserByEmail(username).get();
        if (userModel.getStatus() == 0) {
            response.setStatus(403);
            return;
        } else {
            userModel.setAttempts(0);
            userService.saveUser(userModel);
        }
        String token = userTokenProvider.provide(user.getUsername());
        response.setHeader(ACCESS_TOKEN, token);
        response.addHeader("Vary", "Access-Control-Expose-Headers");
        response.setHeader("Access-Control-Expose-Headers", ACCESS_TOKEN);
    }
}