package com.security.CookieBasedAuth.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.security.CookieBasedAuth.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto {

    private String username;


    private String email;

    private String password;


    private Set<Role> roles;
}
