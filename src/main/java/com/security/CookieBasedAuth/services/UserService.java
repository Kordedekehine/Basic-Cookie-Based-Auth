package com.security.CookieBasedAuth.services;

import com.security.CookieBasedAuth.dtos.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {

    UserResponseDto signUp (SignupRequestDto userSignUpRequest);

    JwtResponseDto login (LoginRequestDto userLoginRequestDto, HttpServletResponse response);

    JwtResponseDto refreshToken (RefreshTokenRequestDto refreshTokenDto);

    String logOut (HttpServletRequest httpServletRequest);

}
