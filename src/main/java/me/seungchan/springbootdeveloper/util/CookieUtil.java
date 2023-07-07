package me.seungchan.springbootdeveloper.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.SerializationUtils;

import java.util.Base64;


public class CookieUtil {

    // 요청값(이름, 값, 만료 기간)을 바탕으로 쿠키 추가.
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value); // 쿠키 생성
        cookie.setPath("/");
        cookie.setMaxAge(maxAge); // 유효기간 설정

        response.addCookie(cookie); // HTTP 응답에 추가.
    }

    // 쿠키의 이름을 입력받아 쿠키 삭제
    // 쿠키 이름을 입력받아 쿠키를 삭제한다.
    // 실제로 삭제하는 방법은 없으므로 파라미터로 넘어온 키의 쿠키를 빈 값으로 바꾸고 만료 시간을 0으로 설정해
    // 쿠키가 재생성 되자마자 만료 처리
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return;
        }

        for (Cookie cookie : cookies) { // 쿠키 리스트을 순환
            if (name.equals(cookie.getName())) { // 쿠키 이름중 찾는 것이 맞다면
                cookie.setValue("");
                cookie.setPath("/");
                cookie.setMaxAge(0); // 유효기간 0 으로 설정해 생성과 동시에 소멸
                response.addCookie(cookie); // HTTP 응답에 추가한다.
            }
        }
    }

    // 객체를 직렬화해 쿠키의 값으로 들어갈 값으로 변환한다.
    public static String serialize(Object obj) {
        return Base64.getUrlEncoder()
                .encodeToString(SerializationUtils.serialize(obj));
    }

    // 쿠키를 역직렬화 객체로 변환합니다.
    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        return cls.cast(
                SerializationUtils.deserialize(
                        Base64.getUrlDecoder().decode(cookie.getValue())
                )
        );
    }
}

