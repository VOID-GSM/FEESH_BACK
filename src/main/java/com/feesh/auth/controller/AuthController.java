package com.feesh.auth.controller;

import com.feesh.auth.dto.SignupRequest;
import com.feesh.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.feesh.auth.dto.LoginRequest;
import com.feesh.auth.dto.LoginResponse;
import com.feesh.auth.dto.CheckEmailRequest;
import com.feesh.auth.dto.CheckEmailResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public String signup(@Valid @RequestBody SignupRequest request) {
        authService.signup(request);
        return "회원가입 성공";
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