package com.feesh.domain.auth.service;

import com.feesh.domain.auth.dto.CheckEmailRequest;
import com.feesh.domain.auth.dto.CheckEmailResponse;
import com.feesh.domain.auth.dto.LoginRequest;
import com.feesh.domain.auth.dto.LoginResponse;
import com.feesh.domain.auth.dto.SignupRequest;
import com.feesh.domain.auth.dto.SignupResponse;
import com.feesh.global.exception.CustomException;
import com.feesh.global.exception.ErrorCode;
import com.feesh.domain.user.entity.User;
import com.feesh.domain.user.repository.UserRepository;
import com.feesh.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public SignupResponse signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.EMAIL_DUPLICATE);
        }
        if (userRepository.existsByNickname(request.getNickname())) {
            throw new CustomException(ErrorCode.NICKNAME_DUPLICATE);
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .build();

        userRepository.save(user);
        return new SignupResponse(
                "회원가입 성공",
                user.getEmail(),
                user.getNickname()
        );
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.LOGIN_FAILED));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.LOGIN_FAILED);
        }
        String accessToken = jwtTokenProvider.createToken(user.getId());


        return new LoginResponse(
                "로그인 성공",
                accessToken,
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