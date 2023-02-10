package com.example.InPostPW.services.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.InPostPW.exception.InvalidTokenException;
import com.example.InPostPW.services.TokenService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Optional;

@Service
public class TokenServiceImpl implements TokenService {

    private static final String NO_HEADER = "there is no token in request headers";

    @Value("${jwt.token.secret}")
    private String secretKey = "JWTSecretKeyForInPostService";

    @Override
    public String getSubjectFromToken(String token) throws JSONException {
        Algorithm algorithm = Algorithm.HMAC256(secretKey.getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String payload = new String(decoder.decode(chunks[1]));
        JSONObject jsonObject = new JSONObject(payload);
        return jsonObject.get("sub").toString();
    }

    @Override
    public HttpServletRequest getCurrentRequest() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .filter(ServletRequestAttributes.class::isInstance)
                .map(ServletRequestAttributes.class::cast)
                .map(ServletRequestAttributes::getRequest).get();
    }

    @Override
    public String getTokenFromRequestHeader(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (token == null) {
            throw new InvalidTokenException(NO_HEADER);
        }
        return token;
    }
}
