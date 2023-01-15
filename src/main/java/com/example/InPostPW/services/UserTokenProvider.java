package com.example.InPostPW.services;

import java.util.Map;

public interface UserTokenProvider {

    Map<String, String> provide(String username);
}
