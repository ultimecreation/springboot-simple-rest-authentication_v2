package com.jwt_rest_auth.demo.dto;

import jakarta.validation.constraints.NotEmpty;

public class LoginDto {

    @NotEmpty
    private String email;

    @NotEmpty
    private String password;

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
