package com.feesh.global.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dev")
@RequiredArgsConstructor
@Profile("local")
public class DevAuthController {

    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/token/{userId}")
    public String issueTestToken(@PathVariable Long userId) {
        return jwtTokenProvider.createToken(userId);  // ← createAccessToken → createToken
    }
}


//테스트용 토큰 부여용 파일입니다