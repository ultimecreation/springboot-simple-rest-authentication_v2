package com.jwt_rest_auth.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/home")
    public String index() {
        return "Welcome to admin page";
    }
}
