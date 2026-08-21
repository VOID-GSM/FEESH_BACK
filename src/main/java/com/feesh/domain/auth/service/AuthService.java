package com.feesh.domain.auth.service;

import com.feesh.domain.auth.dto.request.CheckEmailRequest;
import com.feesh.domain.auth.dto.request.LoginRequest;
import com.feesh.domain.auth.dto.request.SignupRequest;
import com.feesh.domain.auth.dto.response.CheckEmailResponse;
import com.feesh.domain.auth.dto.response.LoginResponse;
import com.feesh.domain.auth.dto.response.SignupResponse;
import com.feesh.domain.comment.repository.CommentRepository;
import com.feesh.domain.like.repository.PostLikeRepository;
import com.feesh.domain.notification.repository.NotificationRepository;
import com.feesh.domain.post.repository.PostRepository;
import com.feesh.domain.user.entity.User;
import com.feesh.domain.user.repository.UserRepository;
import com.feesh.global.exception.CustomException;
import com.feesh.global.exception.ErrorCode;
import com.feesh.global.security.JwtTokenProvider;
import com.feesh.domain.post.repository.PostViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailVerificationService emailVerificationService;
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;
    private final NotificationRepository notificationRepository;
    private final PostRepository postRepository;
    private final PostViewRepository postViewRepository;

    @Transactional
    public SignupResponse signup(SignupRequest request) {
        String email = normalizeEmail(request.getEmail());

        if (userRepository.existsByEmail(email)) {
            throw new CustomException(ErrorCode.EMAIL_DUPLICATE);
        }

        emailVerificationService.validateVerified(email);

        if (userRepository.existsByNickname(request.getNickname())) {
            throw new CustomException(ErrorCode.NICKNAME_DUPLICATE);
        }

        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .build();

        userRepository.save(user);
        emailVerificationService.deleteVerification(email);

        return new SignupResponse(
                "회원가입 성공",
                user.getEmail(),
                user.getNickname()
        );
    }

    public LoginResponse login(LoginRequest request) {
        String email = normalizeEmail(request.getEmail());

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new CustomException(ErrorCode.LOGIN_FAILED)
                );

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )) {
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
        String email = normalizeEmail(request.getEmail());
        boolean duplicated = userRepository.existsByEmail(email);

        if (duplicated) {
            return new CheckEmailResponse(
                    true,
                    "이미 사용 중인 이메일입니다."
            );
        }

        return new CheckEmailResponse(
                false,
                "사용 가능한 이메일입니다."
        );
    }

    @Transactional
    public void withdraw(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new CustomException(ErrorCode.USER_NOT_FOUND)
                );

        postLikeRepository.deleteAllByPost_Author_Id(userId);
        postLikeRepository.deleteAllByUser_Id(userId);

        commentRepository.deleteAllByParentIsNotNullAndPost_Author_Id(userId);
        commentRepository.deleteAllByParent_Author_Id(userId);
        commentRepository.deleteAllByPost_Author_Id(userId);
        commentRepository.deleteAllByAuthor_Id(userId);

        notificationRepository.deleteAllByReceiverId(userId);
        notificationRepository.deleteAllBySenderId(userId);

        postViewRepository.deleteAllByPost_Author_Id(userId);
        postViewRepository.deleteAllByUser_Id(userId);

        postRepository.deleteAllByAuthor_Id(userId);

        userRepository.delete(user);
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}