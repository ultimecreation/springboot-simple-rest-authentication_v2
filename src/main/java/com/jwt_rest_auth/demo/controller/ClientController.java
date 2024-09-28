package com.jwt_rest_auth.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientController {

    @PreAuthorize("hasAnyRole('ROLE_CLIENT','ROLE_ADMIN')")
    @GetMapping("/client/home")
    public ResponseEntity<Object> index() {
        return ResponseEntity.ok().body("Welcome to client page");
    }
}
