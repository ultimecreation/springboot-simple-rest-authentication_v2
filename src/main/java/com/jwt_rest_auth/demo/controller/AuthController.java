package com.jwt_rest_auth.demo.controller;

import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jwt_rest_auth.demo.dto.LoginDto;
import com.jwt_rest_auth.demo.dto.RegisterDto;
import com.jwt_rest_auth.demo.entity.AppUser;
import com.jwt_rest_auth.demo.repository.AppUserRepository;
import com.jwt_rest_auth.demo.service.JwtService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/profile/{id}")
    @PreAuthorize("#id == authentication.principal.id || hasAnyRole('CLIENT')")
    public ResponseEntity<Object> profile(@PathVariable int id,
            Authentication authentication,
            HttpServletRequest request) {
        var user = ((AppUser) authentication.getPrincipal());
        var appUser = appUserRepository.findByEmail(user.getEmail());

        var response = new HashMap<String, Object>();
        response.put("Username", authentication.getName());
        response.put("authorities", authentication.getAuthorities());
        response.put("user", appUser);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid @RequestBody RegisterDto registerDto, BindingResult result) {

        if (result.hasErrors()) {
            var errorsList = result.getAllErrors();
            var errorsMap = new HashMap<String, String>();
            for (int i = 0; i < errorsList.size(); i++) {
                var error = (FieldError) errorsList.get(i);
                errorsMap.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errorsMap);
        }

        var bcryptEncoder = new BCryptPasswordEncoder();

        AppUser appUser = new AppUser();
        appUser.setUsername(registerDto.getUsername());
        appUser.setEmail(registerDto.getEmail());
        appUser.setRole("CLIENT");
        appUser.setPassword(bcryptEncoder.encode(registerDto.getPassword()));
        appUser.setCreatedAt(new Date());

        try {

            appUserRepository.save(appUser);

            String jwtToken = jwtService.createJwtToken(appUser);

            var response = new HashMap<String, Object>();
            response.put("token", jwtToken);
            response.put("user", appUser);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("'there was an error");
            e.printStackTrace();
        }

        return ResponseEntity.badRequest().body("Error");
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginDto loginDto, BindingResult result) {

        if (result.hasErrors()) {
            var errorsList = result.getAllErrors();
            var errorsMap = new HashMap<String, String>();
            for (int i = 0; i < errorsList.size(); i++) {
                var error = (FieldError) errorsList.get(i);
                errorsMap.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errorsMap);
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));

            AppUser appUser = appUserRepository.findByEmail(loginDto.getEmail());
            String jwtToken = jwtService.createJwtToken(appUser);

            var response = new HashMap<String, Object>();
            response.put("token", jwtToken);
            // response.put("user", appUser.getId());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("there was an error");
            e.printStackTrace();
        }

        return ResponseEntity.badRequest().body("Bad credentials");
    }
}
