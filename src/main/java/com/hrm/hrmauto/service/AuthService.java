package com.hrm.hrmauto.service;

import com.hrm.hrmauto.dto.LoginRequest;
import com.hrm.hrmauto.dto.LoginResponse;
import com.hrm.hrmauto.entity.User;
import com.hrm.hrmauto.repository.UserRepository;
import com.hrm.hrmauto.security.JwtService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    public AuthService(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            UserDetailsService userDetailsService,
            JwtService jwtService) {

        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user =
                userRepository
                        .findByEmail(request.getEmail())
                        .orElseThrow();

        UserDetails userDetails =
                userDetailsService
                        .loadUserByUsername(
                                request.getEmail()
                        );

        String token =
                jwtService.generateToken(userDetails);

        return new LoginResponse(
                token,
                user.getEmail(),
                user.getRole().name()
        );
    }
}