package me.seungchan.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.seungchan.springbootdeveloper.domain.User;
import me.seungchan.springbootdeveloper.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

// 스프링 시큐리티에서 사용자의 정보를 가져오는 UserDetailsService 인터페이스를 구현합니다.
// 필수로 구현해야 하는 loadUserByUsername을 구현

// 스프링 시큐리티에서 로그인을 진행할 때 사용자 정보를 가져오는 코드를 작성
@RequiredArgsConstructor
@Service
public class UserDetailService implements UserDetailsService {
    // Config/WebSecurityConfig에 있는 authenticationManager에 저장을해서 이용

    private final UserRepository userRepository;

    // user의 이름(email)을 이용해 유저의 정보를 가져온다.
    @Override
    public User loadUserByUsername(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException((email)));
    }
}
