package me.seungchan.springbootdeveloper.controller;

import lombok.RequiredArgsConstructor;
import me.seungchan.springbootdeveloper.dto.CreateAccessTokenRequest;
import me.seungchan.springbootdeveloper.dto.CreateAccessTokenResponse;
import me.seungchan.springbootdeveloper.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// 요청을 받고 처리할 컨트롤러를 생성합니다.
// /api/token POST 요청이 오면 토큰 서비스에서 리프레시 토큰을 기반으로
// 새로운 액세스 토큰을 만들어주면 된다.
@RequiredArgsConstructor
@RestController
public class TokenApiController {

    private final TokenService tokenService;

    @PostMapping("/api/token")
    public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken(@RequestBody CreateAccessTokenRequest request) {
        // 액세스 토큰 만들기
        String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());

        // 응답객체에 새로운 액세스 토큰 객체
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateAccessTokenResponse(newAccessToken));
    }
}
