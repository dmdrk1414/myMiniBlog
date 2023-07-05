package me.seungchan.springbootdeveloper.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.seungchan.springbootdeveloper.dto.AddUserRequest;
import me.seungchan.springbootdeveloper.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class UserApiController {

    private final UserService userService;

    // 회원 가입 처리가 된 다음 로그인 페이지로 이동하기 위해
    // redirect: 접두사를 붙였다.
    // 이렇게 하면 회원 가입 끝나면 강제로 /login URL에 해당하는 화면으로 이동한다.
    @PostMapping("/user")
    public String signup(AddUserRequest request) {
        userService.save(request); // 회원 가입 메서드 호출
        return "redirect:/login"; // 회원 가입이 완료된 이후에 로그인 페이지로 이동
    }

    // /logout GET 요청을 하면 로그아웃을 담당하는 핸들러인 SecurityContextLogoutHandler의
    // logout() 메서드를 호출해서 로그아웃한다.
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response
                , SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login";
    }
}
