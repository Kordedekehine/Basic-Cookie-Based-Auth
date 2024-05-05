package com.security.CookieBasedAuth.controller;

import com.security.CookieBasedAuth.dtos.LoginRequestDto;
import com.security.CookieBasedAuth.dtos.RefreshTokenRequestDto;
import com.security.CookieBasedAuth.dtos.SignupRequestDto;
import com.security.CookieBasedAuth.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody SignupRequestDto signupRequestDto){

        return new ResponseEntity<>(userService.signUp(signupRequestDto),HttpStatus.OK);
    }


    @PostMapping("/login")
    public ResponseEntity<?> logIn(@RequestBody LoginRequestDto userLoginRequestDto, HttpServletResponse response) {

        return new ResponseEntity<>(userService.login(userLoginRequestDto,response), HttpStatus.OK);

    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequestDto refreshTokenDto) {

        return new ResponseEntity<>(userService.refreshToken(refreshTokenDto), HttpStatus.OK);

    }


    @PostMapping("/logOut")
    public ResponseEntity<?> logOut(HttpServletRequest httpServletRequest) {

        return new ResponseEntity<>(userService.logOut(httpServletRequest), HttpStatus.OK);

    }

    @GetMapping("/ping")
    public String test() {
        try {
            return "Welcome";
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

}
