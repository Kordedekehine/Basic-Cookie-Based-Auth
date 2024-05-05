package com.security.CookieBasedAuth.services;


public interface TokenBlackListService {

    void addToBlacklist(String token);
    boolean isBlacklisted(String token);
}


