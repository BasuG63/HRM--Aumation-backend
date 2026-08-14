package com.hrm.hrmauto.config;

import com.hrm.hrmauto.entity.Role;
import com.hrm.hrmauto.entity.User;
import com.hrm.hrmauto.repository.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        // ==========================================
        // HR USER
        // ==========================================

        if (userRepository
                .findByEmail("hr@hrm.com")
                .isEmpty()) {

            User hr = new User();

            hr.setEmail("hr@hrm.com");

            hr.setPassword(
                    passwordEncoder.encode("Hr@123")
            );

            hr.setRole(Role.HR);

            userRepository.save(hr);

            System.out.println(
                    "HR user created successfully"
            );
        }

        // ==========================================
        // EMPLOYEE USER
        // ==========================================
        //
        // Employees are created through
        // EmployeeService.createEmployee()
        //
        // Therefore, we DO NOT create an employee
        // here.
        //
        // EmployeeService automatically creates:
        //
        // User
        //   ↓
        // Role.EMPLOYEE
        //   ↓
        // Employee
        //
        // ==========================================
    }
}