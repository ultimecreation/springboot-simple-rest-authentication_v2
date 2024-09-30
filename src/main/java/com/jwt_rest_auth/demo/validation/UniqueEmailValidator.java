package com.jwt_rest_auth.demo.validation;

import org.springframework.beans.factory.annotation.Autowired;

import com.jwt_rest_auth.demo.entity.AppUser;
import com.jwt_rest_auth.demo.repository.AppUserRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmailConstraint, String> {

    @Autowired
    private AppUserRepository appUserRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        AppUser inDB = appUserRepository.findByEmail(value);
        if (inDB == null) {
            return true;
        }

        return false;
    }

}