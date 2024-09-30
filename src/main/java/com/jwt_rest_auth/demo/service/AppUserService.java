package com.jwt_rest_auth.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jwt_rest_auth.demo.repository.AppUserRepository;

@Service
public class AppUserService implements UserDetailsService {

    @Autowired
    AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDetails appUser = appUserRepository.findByEmail(email);

        return (appUser == null) ? null : appUser;

        // var springUser = User.withUsername(appUser.getEmail())
        // .password(appUser.getPassword())
        // .roles(appUser.getRole())
        // .build();
        // return springUser;
    }

}
