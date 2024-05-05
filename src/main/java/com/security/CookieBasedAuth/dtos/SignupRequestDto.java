package com.security.CookieBasedAuth.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.security.CookieBasedAuth.entity.Role;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignupRequestDto {

    private String username;


    private String email;

    private String password;

    private String confirmPassword;

    @JsonProperty("roles")
    private Set<Role> roles;
}
