package me.seungchan.springbootdeveloper.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserViewController {
    // /login 경로로 접근하면 login() 메서드가 login.html
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // /signup 경로에 접근하면 signup() 메서드는 singnup.html를 반환
    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }
}
