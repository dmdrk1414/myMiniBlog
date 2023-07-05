package me.seungchan.springbootdeveloper.controller.config.jwt;


import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Builder;
import lombok.Getter;
import me.seungchan.springbootdeveloper.config.jwt.JwtProperties;

import java.time.Duration;
import java.util.Date;
import java.util.Map;

import static java.util.Collections.emptyMap;

// 이 파일은 JWT 토큰 서비스를 테스트하는 데 사용할 모킹(mocking)용 객체입니다.
// 모킹이란 테스트를 실행할 때 객체를 대신하는 가짜 객체을 말한다.

@Getter
public class JwtFactory { // 모킹용(가짜 객체)

    private String subject = "test@email.com";

    private Date issuedAt = new Date();

    private Date expiration = new Date(new Date().getTime() + Duration.ofDays(14).toMillis());

    private Map<String, Object> claims = emptyMap();

    // 매개 변수 있으면 사용, 없으면 매개 변수
    @Builder
    public JwtFactory(String subject, Date issuedAt, Date expiration,
                      Map<String, Object> claims) {
        this.subject = subject != null ? subject : this.subject;
        this.issuedAt = issuedAt != null ? issuedAt : this.issuedAt;
        this.expiration = expiration != null ? expiration : this.expiration;
        this.claims = claims != null ? claims : this.claims;
    }

    public static JwtFactory withDefaultValues() {
        return JwtFactory.builder().build();
    }

    // jjwt 라이브러리를 사용해 JWT 토큰 생성
    public String createToken(JwtProperties jwtProperties) {
        // 토큰을 만들어 반환을 한다.
        return Jwts.builder()
                .setSubject(subject)
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }
}

