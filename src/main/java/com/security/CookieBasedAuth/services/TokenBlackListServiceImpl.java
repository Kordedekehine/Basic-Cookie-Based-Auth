package com.security.CookieBasedAuth.services;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service   //InMemory Token Black List
public class TokenBlackListServiceImpl implements TokenBlackListService{

    private final Set<String> blacklist = new HashSet<>();

    @Override
    public void addToBlacklist(String token) {
        blacklist.add(token);
    }

    @Override
    public boolean isBlacklisted(String token) {
        return blacklist.contains(token);
    }
}
