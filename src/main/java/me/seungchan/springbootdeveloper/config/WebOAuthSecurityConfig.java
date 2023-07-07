package me.seungchan.springbootdeveloper.config;


import lombok.RequiredArgsConstructor;
import me.seungchan.springbootdeveloper.config.jwt.TokenAuthenticationFilter;
import me.seungchan.springbootdeveloper.config.jwt.TokenProvider;
import me.seungchan.springbootdeveloper.config.oauth.OAuth2AuthorizationRequestBasedOnCookieRepository;
import me.seungchan.springbootdeveloper.config.oauth.OAuth2SuccessHandler;
import me.seungchan.springbootdeveloper.config.oauth.OAuth2UserCustomService;
import me.seungchan.springbootdeveloper.repository.RefreshTokenRepository;
import me.seungchan.springbootdeveloper.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@RequiredArgsConstructor
@Configuration
public class WebOAuthSecurityConfig {

    private final OAuth2UserCustomService oAuth2UserCustomService;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;

    // 스프링 시큐리티 기능 비활성화
    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                .requestMatchers(toH2Console())
                .requestMatchers("/img/**", "/css/**", "/js/**");
    }

    // 토큰 방식으로 인증을 하므로 기존 폼 로그인, 세션 기능을 비활성화합니다.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 1. 토큰 방식으로 인증을 하기 때문에 기존에 사용하던 폼로그인, 세션 비활성화
        http.csrf().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .logout().disable();

        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // 2. 헤더를 확인할 커스텀 필터 추가
        // 헤더값을 확인할 커스텀 필터를 추가합니다.
        // 이 필터는 9.2.4 '토큰 필터 구현하기'에서 구현한 TokenAuthenticationFiler.class 입니다.
        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);


        // 3. 토큰 재발급 URL은 인증 없이 접근 가능하도록 설정. 나머지 API URL은 인증 필요
        // 토큰 재발급 URL은 인증 없이 접근하도록 설정하고 나머지 API들은 모두 인증을 해야 접근하도록 설정합니다.
        http.authorizeRequests()
                // 토큰 재발급 URL은 인증 없이 접근하도록 설정하고
                .requestMatchers("/api/token").permitAll()
                // 나머지 API들은 인증을 해야 접근하도록 설정한다.
                .requestMatchers("/api/**").authenticated()
                .anyRequest().permitAll();

        http.oauth2Login()
                .loginPage("/login")
                .authorizationEndpoint()
                // 4. Authorization 요청과 관련된 상태 저장
                // Oauth2에 필요한 정보를 세션이 아닌 쿠키에 저장해서 쓸 수 있도록 인증 요청과 관련된 상태를 저장할 저장소를 설정한다.
                .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository())
                .and()
                // 인증 성공시 실행할 핸들러도 설정합니다.
                // 해당 클래스는 아직 구현하지 않았으므로 에러가 발생하지 않았으므로 에러가 발생할 겂니다.
                .successHandler(oAuth2SuccessHandler()) // 5. 인증 성공 시 실행할 핸들러
                .userInfoEndpoint()
                .userService(oAuth2UserCustomService);

        http.logout()
                .logoutSuccessUrl("/login");


        // 6. /api 로 시작하는 url인 경우 401 상태 코드를 반환하도록 예외 처리
        // /api로 시작하는 url인 경우 인증 실패 시 401 상태 코드, 즉, Unauthorized를 반환합니다.
        http.exceptionHandling()
                .defaultAuthenticationEntryPointFor(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                        new AntPathRequestMatcher("/api/**"));


        return http.build();
    }


    @Bean
    public OAuth2SuccessHandler oAuth2SuccessHandler() {
        return new OAuth2SuccessHandler(tokenProvider,
                refreshTokenRepository,
                oAuth2AuthorizationRequestBasedOnCookieRepository(),
                userService
        );
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider);
    }

    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
