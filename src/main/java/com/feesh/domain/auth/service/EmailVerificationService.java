package com.feesh.domain.auth.service;

import com.feesh.domain.auth.dto.request.SendEmailCodeRequest;
import com.feesh.domain.auth.dto.request.VerifyEmailCodeRequest;
import com.feesh.domain.auth.dto.response.SendEmailCodeResponse;
import com.feesh.domain.auth.dto.response.VerifyEmailCodeResponse;
import com.feesh.domain.auth.entity.EmailVerification;
import com.feesh.domain.auth.repository.EmailVerificationRepository;
import com.feesh.domain.user.repository.UserRepository;
import com.feesh.global.exception.CustomException;
import com.feesh.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private static final int CODE_EXPIRATION_MINUTES = 5;
    private static final int VERIFICATION_EXPIRATION_MINUTES = 30;

    private final EmailVerificationRepository emailVerificationRepository;
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;

    private final SecureRandom secureRandom = new SecureRandom();

    @Value("${spring.mail.username}")
    private String mailUsername;

    @Transactional
    public SendEmailCodeResponse sendCode(SendEmailCodeRequest request) {
        String email = normalizeEmail(request.getEmail());

        if (userRepository.existsByEmail(email)) {
            throw new CustomException(ErrorCode.EMAIL_DUPLICATE);
        }

        String code = generateCode();
        LocalDateTime expiresAt =
                LocalDateTime.now().plusMinutes(CODE_EXPIRATION_MINUTES);

        EmailVerification verification = emailVerificationRepository
                .findByEmail(email)
                .orElseGet(() -> new EmailVerification(email, code, expiresAt));

        if (verification.getId() != null) {
            verification.updateCode(code, expiresAt);
        }

        emailVerificationRepository.save(verification);

        sendEmail(email, code);

        return new SendEmailCodeResponse(
                "인증번호가 이메일로 전송되었습니다."
        );
    }

    @Transactional
    public VerifyEmailCodeResponse verifyCode(VerifyEmailCodeRequest request) {
        String email = normalizeEmail(request.getEmail());

        EmailVerification verification = emailVerificationRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new CustomException(ErrorCode.EMAIL_CODE_NOT_FOUND));

        if (verification.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new CustomException(ErrorCode.EMAIL_CODE_EXPIRED);
        }

        if (!verification.getCode().equals(request.getCode())) {
            throw new CustomException(ErrorCode.EMAIL_CODE_INVALID);
        }

        verification.verify();

        return new VerifyEmailCodeResponse(
                true,
                "이메일 인증이 완료되었습니다."
        );
    }

    @Transactional(readOnly = true)
    public void validateVerified(String requestedEmail) {
        String email = normalizeEmail(requestedEmail);

        EmailVerification verification = emailVerificationRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new CustomException(ErrorCode.EMAIL_NOT_VERIFIED));

        if (!verification.isVerified() || verification.getVerifiedAt() == null) {
            throw new CustomException(ErrorCode.EMAIL_NOT_VERIFIED);
        }

        LocalDateTime verificationExpiresAt = verification.getVerifiedAt()
                .plusMinutes(VERIFICATION_EXPIRATION_MINUTES);

        if (verificationExpiresAt.isBefore(LocalDateTime.now())) {
            throw new CustomException(
                    ErrorCode.EMAIL_VERIFICATION_EXPIRED
            );
        }
    }

    @Transactional
    public void deleteVerification(String requestedEmail) {
        String email = normalizeEmail(requestedEmail);
        emailVerificationRepository.deleteByEmail(email);
    }

    private void sendEmail(String email, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailUsername);
        message.setTo(email);
        message.setSubject("[FEESH] 이메일 인증번호 안내");
        message.setText(
                "FEESH 이메일 인증번호는 " + code + "입니다.\n\n"
                        + "인증번호는 "
                        + CODE_EXPIRATION_MINUTES
                        + "분 동안 유효합니다."
        );

        try {
            mailSender.send(message);
        } catch (MailException exception) {
            throw new CustomException(ErrorCode.EMAIL_SEND_FAILED);
        }
    }

    private String generateCode() {
        int number = secureRandom.nextInt(1_000_000);
        return String.format("%06d", number);
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}