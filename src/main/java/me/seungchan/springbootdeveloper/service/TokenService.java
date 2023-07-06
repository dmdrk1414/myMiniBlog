package me.seungchan.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.seungchan.springbootdeveloper.config.jwt.TokenProvider;
import me.seungchan.springbootdeveloper.domain.User;
import org.springframework.stereotype.Service;

import java.time.Duration;

// 이제 토큰 서비스 클래스를 생성한다.
// service 디렉터리에 TokenService.java 파일을 생성
// createNewAccessToken()메서드는 전달받은 리프리시 토큰으로 토큰 유효성 검사를 진행
// 유효한 리프레시 토큰일 때 리프레시 토큰으로 사용자 ID를 찾는다.
// 마지막으로는 상용자 ID로 사용자를 찾은 후에 토큰 제공자의 generateToken() 메서드를 호출해서
// 새로운 액세스 토큰을 생성
@RequiredArgsConstructor
@Service
public class TokenService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    public String createNewAccessToken(String refreshToken) {
        // 토큰 유효성 검사에 실패하면 예외 발생
        if(!tokenProvider.validToken(refreshToken)) {
            throw new IllegalArgumentException("Unexpected token");
        }

        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        User user = userService.findById(userId);

        return tokenProvider.generateToken(user, Duration.ofHours(2));
    }
}

