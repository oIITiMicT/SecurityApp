package com.example.InPostPW.filter;


import com.example.InPostPW.model.User;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;


@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final String ACCESS_TOKEN = "accessToken";
    private static final String REFRESH_TOKEN = "refreshToken";

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    private final UserTokenProvider userTokenProvider;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = null;
        String password;
        Authentication authentication = null;
        String salt = "";

        try {
            JsonNode json = new ObjectMapper().readTree(request.getReader());
            if (!(json.hasNonNull("email") && json.hasNonNull("password"))) {
                throw new AuthenticationServiceException("Invalid request. Expected JSON with 'email' and 'password' fields.");
            }
            username = json.get("email").asText();
            password = json.get("password").asText();
            Optional<User> user = userService.findUserByEmail(username);
            if (user.isPresent()) {
                salt = user.get().getSalt();
            }
            password = salt + password;
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
        UserDetails user = (UserDetails)authResult.getPrincipal();
        Map<String, String> tokens = userTokenProvider.provide(user.getUsername());
        response.setHeader(ACCESS_TOKEN, tokens.get(ACCESS_TOKEN));
        response.setHeader(REFRESH_TOKEN, tokens.get(REFRESH_TOKEN));
        response.addHeader("Vary", "Access-Control-Expose-Headers");
        response.setHeader("Access-Control-Expose-Headers", ACCESS_TOKEN);
        response.setHeader("Access-Control-Expose-Headers", REFRESH_TOKEN);
    }
}