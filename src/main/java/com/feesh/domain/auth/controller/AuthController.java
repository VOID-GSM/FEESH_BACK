package com.feesh.domain.auth.controller;

import com.feesh.domain.auth.dto.SignupRequest;
import com.feesh.domain.auth.dto.SignupResponse;
import com.feesh.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.feesh.domain.auth.dto.LoginRequest;
import com.feesh.domain.auth.dto.LoginResponse;
import com.feesh.domain.auth.dto.CheckEmailRequest;
import com.feesh.domain.auth.dto.CheckEmailResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public SignupResponse signup(@Valid @RequestBody SignupRequest request) {
        return authService.signup(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/check-email")
    public CheckEmailResponse checkEmail(@Valid @RequestBody CheckEmailRequest request) {
        return authService.checkEmail(request);
    }
}