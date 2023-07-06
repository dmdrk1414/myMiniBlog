package me.seungchan.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.seungchan.springbootdeveloper.domain.RefreshToken;
import me.seungchan.springbootdeveloper.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

// service 디렉터리에 RefreshTokenService.java
// 리프레시 토큰으로 리프레시 토큰 객체를 검색해서
// 전달하는 findByRefreshToken() 메서드를 구현
@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    // refreshToken을 이용해 RefreshToken을 가져온다.
    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected token"));
    }
}

