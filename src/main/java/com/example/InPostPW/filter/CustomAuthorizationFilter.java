package com.example.InPostPW.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.InPostPW.services.UserTokenProvider;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private static final String ACCESS_TOKEN = "accessToken";
    private static final String REFRESH_TOKEN = "refreshToken";

    private final UserTokenProvider userTokenProvider;
    private final String secret;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String accessToken = request.getHeader("accessToken");
        String refreshToken = request.getHeader("refreshToken");
        if (request.getServletPath().matches("/api/login|/api/registration|/api/note/new|/api/note/get")
        ) {
            filterChain.doFilter(request, response);
        } else {
            if (accessToken == null) {
                response.getOutputStream().print("No token header.");
                response.setStatus(403);
                return;
            }
            try {
                Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(accessToken);
                String username = decodedJWT.getSubject();
                String[] roles = decodedJWT.getClaim("role").asArray(String.class);
                List<GrantedAuthority> authorities
                        = Arrays.stream(roles).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                filterChain.doFilter(request, response);
            } catch (Exception e) {
                if (e.getClass().getSimpleName().equals("TokenExpiredException")) {
                    try {
                        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
                        JWTVerifier verifier = JWT.require(algorithm).build();
                        DecodedJWT decoded = JWT.decode(accessToken);
                        String subjectAccess = decoded.getSubject();
                        DecodedJWT decodedJWT = verifier.verify(refreshToken);
                        String subjectRefresh = decodedJWT.getSubject();
                        if (subjectRefresh.equals(subjectAccess)) {
                            Map<String, String> tokens = userTokenProvider.provide(subjectAccess);
                            response.setHeader(ACCESS_TOKEN, tokens.get(ACCESS_TOKEN));
                            response.setHeader(REFRESH_TOKEN, tokens.get(REFRESH_TOKEN));
                            response.addHeader("Vary", "Access-Control-Expose-Headers");
                            response.setHeader("Access-Control-Expose-Headers", ACCESS_TOKEN);
                            response.setHeader("Access-Control-Expose-Headers", REFRESH_TOKEN);
                            String[] roles = decodedJWT.getClaim("role").asArray(String.class);
                            List<GrantedAuthority> authorities
                                    = Arrays.stream(roles).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
                            UsernamePasswordAuthenticationToken authenticationToken =
                                    new UsernamePasswordAuthenticationToken(subjectAccess, null, authorities);
                            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                            filterChain.doFilter(request, response);
                        } else {
                            System.out.println("HERE");
                            response.setStatus(403);
                            response.getWriter().println("Error with token.");
                        }
                    } catch (Exception exception) {
                        System.out.println(exception);
                        System.out.println("OR HERE");
                        response.setStatus(403);
                        response.getWriter().println("Error with token.");
                    }
                } else {
                    System.out.println("THEN HERE");
                    response.setStatus(403);
                    response.getWriter().println("Error with token.");
                }
            }
        }
    }
}

