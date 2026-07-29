package com.feesh.domain.auth.controller;

import com.feesh.domain.auth.dto.request.CheckEmailRequest;
import com.feesh.domain.auth.dto.request.LoginRequest;
import com.feesh.domain.auth.dto.request.SendEmailCodeRequest;
import com.feesh.domain.auth.dto.request.SignupRequest;
import com.feesh.domain.auth.dto.request.VerifyEmailCodeRequest;
import com.feesh.domain.auth.dto.response.CheckEmailResponse;
import com.feesh.domain.auth.dto.response.LoginResponse;
import com.feesh.domain.auth.dto.response.SendEmailCodeResponse;
import com.feesh.domain.auth.dto.response.SignupResponse;
import com.feesh.domain.auth.dto.response.VerifyEmailCodeResponse;
import com.feesh.domain.auth.service.AuthService;
import com.feesh.domain.auth.service.EmailVerificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final EmailVerificationService emailVerificationService;

    @PostMapping("/signup")
    public SignupResponse signup(
            @Valid @RequestBody SignupRequest request
    ) {
        return authService.signup(request);
    }

    @PostMapping("/login")
    public LoginResponse login(
            @Valid @RequestBody LoginRequest request
    ) {
        return authService.login(request);
    }

    @PostMapping("/check-email")
    public CheckEmailResponse checkEmail(
            @Valid @RequestBody CheckEmailRequest request
    ) {
        return authService.checkEmail(request);
    }

    @PostMapping("/send-email-code")
    public SendEmailCodeResponse sendEmailCode(
            @Valid @RequestBody SendEmailCodeRequest request
    ) {
        return emailVerificationService.sendCode(request);
    }

    @PostMapping("/verify-email-code")
    public VerifyEmailCodeResponse verifyEmailCode(
            @Valid @RequestBody VerifyEmailCodeRequest request
    ) {
        return emailVerificationService.verifyCode(request);
    }

   @DeleteMapping("/withdraw")
    public ResponseEntity<Void> withdraw(
            @AuthenticationPrincipal Long userId
    ) {
        authService.withdraw(userId);
        return ResponseEntity.noContent().build();
    }
}