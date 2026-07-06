package com.feesh.auth.service;

import com.feesh.auth.dto.CheckEmailRequest;
import com.feesh.auth.dto.CheckEmailResponse;
import com.feesh.auth.dto.LoginRequest;
import com.feesh.auth.dto.LoginResponse;
import com.feesh.auth.dto.SignupRequest;
import com.feesh.global.exception.CustomException;
import com.feesh.global.exception.ErrorCode;
import com.feesh.user.entity.User;
import com.feesh.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.EMAIL_DUPLICATE);
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .build();

        userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.LOGIN_FAILED));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.LOGIN_FAILED);
        }

        return new LoginResponse(
                "로그인 성공",
                user.getEmail(),
                user.getNickname()
        );
    }

    public CheckEmailResponse checkEmail(CheckEmailRequest request) {
        boolean duplicated = userRepository.existsByEmail(request.getEmail());

        if (duplicated) {
            return new CheckEmailResponse(true, "이미 사용 중인 이메일입니다.");
        }

        return new CheckEmailResponse(false, "사용 가능한 이메일입니다.");
    }
}