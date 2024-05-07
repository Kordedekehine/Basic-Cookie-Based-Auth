package com.security.CookieBasedAuth.services;

import com.security.CookieBasedAuth.entity.RefreshToken;
import com.security.CookieBasedAuth.entity.User;
import com.security.CookieBasedAuth.repository.RefreshTokenRepository;
import com.security.CookieBasedAuth.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class RefreshTokenService {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    UserRepository userRepository;

    public RefreshToken createRefreshToken(String username){
        RefreshToken refreshToken = RefreshToken.builder()
                .user(userRepository.findByUsername(username))
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(3600000))
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    @CachePut(value = "refreshed_updated_token" )
    public RefreshToken createOrUpdateRefreshToken(String username) {
        User user = userRepository.findByUsername(username);
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(user.getId());

        if (refreshToken == null) {

            refreshToken = RefreshToken.builder()
                    .user(user)
                    .token(UUID.randomUUID().toString())
                    .expiryDate(Instant.now().plusMillis(3600000 * 3)) // Set expiry time to 1 hour from now
                    .build();
        } else {

            refreshToken.setExpiryDate(Instant.now().plusMillis(3600000 * 3));
        }

        return refreshTokenRepository.save(refreshToken);
    }


    public Optional<RefreshToken> findByToken(String token){

        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByToken(token);
        if (refreshToken.isEmpty()){
            throw new RuntimeException("INVALID TOKEN");
        }

        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token){
        if(token.getExpiryDate().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh token is expired. Please make a new login..!");
        }
        return token;

    }

}
