package com.jwt_rest_auth.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jwt_rest_auth.demo.entity.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, Integer> {

    public AppUser findByEmail(String email);
}
