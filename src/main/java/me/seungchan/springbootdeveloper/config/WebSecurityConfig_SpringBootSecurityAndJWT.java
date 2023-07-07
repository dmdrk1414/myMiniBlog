package me.seungchan.springbootdeveloper.config;

import lombok.RequiredArgsConstructor;
import me.seungchan.springbootdeveloper.service.UserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig_SpringBootSecurityAndJWT {

    private final UserDetailService userService;
    // p212

    // 1. 스프링 시큐리티 기능 비활성화
    // 스프링 시큐리티의 모든 기능을 사용하지 않게 설정하는 코드
    // 인증, 인가 서비스를 모든 곳에 모두 적용하지 않는다.
    //      일반적으로 정적 리소스(이미지, HTML 파일)에 설정한다.
    //      정적 리소스만 스프링 시큐리티 사용을 비활성화하는데 static 하위 경로에 있는
    //      리소스와 h2의 데이터를 확인하는 데 사용하는 h2-console 하위 url을 대상으로 ignoring() 메서드를 사용
    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                .requestMatchers(toH2Console())
                .requestMatchers("/static/**");
    }

    // 2. 특정 HTTP 요청에 대한 웹 기반 보안 구성
    // 특정 HTTP 요청에 대해 웹 기반 보안을 구성한다.
    // 이 메서드에서 인증/인가 및 로그인, 로그아웃 관련 설정할 수 있다.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // 3. 인증, 인가 설정 특정 경로에 대한 액세스 설정을 한다.
                .authorizeRequests()
                    // 특정 요청과 일치하는 url에 대한 액세스를 설정한다.
                    .requestMatchers("/login", "/signup", "/user")
                    // 누구나 접근이 가능하게 설정한다. 즉 "/login", "/signup", "/user" 로 요청이 오면 인증/인가 없이도 접근할 수 있다.
                    .permitAll()
                    // 위에서 설정한 url 이외의 요청에 대해서 설정한다.
                    .anyRequest()
                    // 별도의 인가는 필요하지 않지만 인증이 접근할 수 잇다.
                    .authenticated()
                .and()
                // -------------------------------------
                // 4. 폼 기반 로그인 설정
                .formLogin()
                    // 로그인 페이지 경로를 설정한다.
                    .loginPage("/login")
                    // 로그인이 완료되었을 때 이동할 경로를 설정한다.
                    .defaultSuccessUrl("/articles")
                .and()
                // -------------------------------------
                // 5. 로그아웃 설정
                .logout()
                    // 로그아웃
                    // 로그아웃이 완료되었을 때 이동할 경로를 설정한다.
                    .logoutSuccessUrl("/login")
                    // 로그아웃 이후에 세션을 전체 삭제할지 여부를 설정한다.
                    .invalidateHttpSession(true)
                .and()
                .csrf().disable() // 6. csrf 비활성화 TODO : 활성화하기
                .build();
    }

    // 7. 인증 관리자 관련 설정
    // 인증 관리자 관련 설정
    // 사용자 정보를 가져올 서비스를 재정의하거나, 인증 방법, 예를 들어
    // LDAP, JDBC 기반 인증 등을 설정할 때 사용한다.
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailService userDetailService) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                // 8. 사용자 정보 서비스 설정
                // 사용자 정보를 가져올 서비스를 설정,
                // 이때 설정하는 서비스 클래스는 반드시 UserDetailsService를 상속받은 클래스여야 한다.
                .userDetailsService(userService)
                // 비밀번호를 암호화하기 위한 인코더를 설정
                .passwordEncoder(bCryptPasswordEncoder)
                .and()
                .build();
    }

    // 9. 패스워드 인코더로 사용할 빈 등록
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
