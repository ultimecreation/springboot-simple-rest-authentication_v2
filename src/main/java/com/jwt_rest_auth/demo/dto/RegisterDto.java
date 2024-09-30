package com.jwt_rest_auth.demo.dto;

import java.sql.Date;

import org.springframework.data.annotation.Transient;

import com.jwt_rest_auth.demo.validation.FieldMatch;
import com.jwt_rest_auth.demo.validation.UniqueEmailConstraint;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@FieldMatch(first = "password", second = "confirmPassword", message = "Password and Confirm Password do not match")
public class RegisterDto {

    @NotEmpty
    private String username;

    @NotEmpty
    @UniqueEmailConstraint
    private String email;

    @NotEmpty(message = "password is required")
    private String password;

    @Transient

    @NotEmpty(message = "password Confirmation is required")
    private String confirmPassword;

    private String Role;

    private Date createdAt;

}