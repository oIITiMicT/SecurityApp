package com.example.InPostPW.services;

import org.json.JSONException;
import javax.servlet.http.HttpServletRequest;

public interface TokenService {

    String getSubjectFromToken(String token) throws JSONException;

    HttpServletRequest getCurrentRequest();

    String getTokenFromRequestHeader(HttpServletRequest request);
}
