package me.seungchan.springbootdeveloper.config.oauth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.seungchan.springbootdeveloper.config.jwt.TokenProvider;
import me.seungchan.springbootdeveloper.domain.RefreshToken;
import me.seungchan.springbootdeveloper.domain.User;
import me.seungchan.springbootdeveloper.repository.RefreshTokenRepository;
import me.seungchan.springbootdeveloper.service.UserService;
import me.seungchan.springbootdeveloper.util.CookieUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;

// 이 클래스는 OAuth2 인증이 성공적으로 완료되었을 때 실행되는 핸들러를 정의합니다.
// 여기서는 인증 성공 후 사용자에게 토큰을 제공하고, 토큰을 저장하며, 사용자를 적절한 URL로 리다이렉트하는 작업을 수행합니다.
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token"; // 리프레쉬 토큰의 이름
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14); // 리프레쉬 토큰의 유효기간
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofDays(1); // 액세스 토큰의 유효기간
    public static final String REDIRECT_PATH = "/articles"; // 리 다이렉트를 하는 URL

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;
    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // OAuth2 인증이 성공적으로 완료된 후 호출되는 메서드
        // 인증된 사용자 정보를 가져옵니다.
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        // 이메일을 기반으로 사용자 정보를 조회
        User user = userService.findByEmail((String) oAuth2User.getAttributes().get("email"));

        // 새로운 리프레쉬 토큰을 생성한다.
        String refreshToken = tokenProvider.generateToken(user, REFRESH_TOKEN_DURATION);
        // 리프레쉬 토큰을 db에 저장한다.
        saveRefreshToken(user.getId(), refreshToken);
        // 리프레쉬 토큰을 쿠키에 추가한다.
        addRefreshTokenToCookie(request, response, refreshToken);

        // 새로운 access 토큰을 생성한다.
        String accessToken = tokenProvider.generateToken(user, ACCESS_TOKEN_DURATION);
        // 사용자를 리다이렉트할 URL을 얻는다.
        String targetUrl = getTargetUrl(accessToken);

        // 인증 관련 속성을 지운다.
        clearAuthenticationAttributes(request, response);

        // 사용자를 리다이렉트한다.
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    // 사용자 ID를 기반으로 refresh 토큰을 저장하거나 갱신한다.
    private void saveRefreshToken(Long userId, String newRefreshToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId)
                .map(entity -> entity.update(newRefreshToken))
                .orElse(new RefreshToken(userId, newRefreshToken));

        refreshTokenRepository.save(refreshToken);
    }

    // refresh 토큰을 쿠키에 추가한다.
    private void addRefreshTokenToCookie(HttpServletRequest request, HttpServletResponse response, String refreshToken) {
        int cookieMaxAge = (int) REFRESH_TOKEN_DURATION.toSeconds();

        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN_COOKIE_NAME);
        CookieUtil.addCookie(response, REFRESH_TOKEN_COOKIE_NAME, refreshToken, cookieMaxAge);
    }

    // 인증 요청에 대한 쿠키를 삭제한다.
    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    // 인증 후 사용자를 리다이렉트할 URL을 생성한다.
    private String getTargetUrl(String token) {
        return UriComponentsBuilder.fromUriString(REDIRECT_PATH)
                .queryParam("token", token)
                .build()
                .toUriString();
    }
}
