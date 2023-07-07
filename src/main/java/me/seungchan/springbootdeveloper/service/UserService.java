package me.seungchan.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.seungchan.springbootdeveloper.domain.User;
import me.seungchan.springbootdeveloper.dto.AddUserRequest;
import me.seungchan.springbootdeveloper.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 서비스 계층에서 레포지토리에 dto의 정보를 저장
    public Long save(AddUserRequest dto) {
        // OAuth2 방식의 BCryptPasswordEncoder 방식
        // BCryptPasswordEncoder를 생성자를 사용해 직접 생성해서 패스워드를 암호화할 수 있게 한다.
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return userRepository.save(
                        User.builder()
                                .email(dto.getEmail())
                                // 패스워드 암호화
                                // 패스워드를 저장할 때 시큐리티를 설정하며 패스워드 인코딩용으로 등록한 빈을 사용해서
                                // 암호화 한후에 저장한다.
        //                      .password(bCryptPasswordEncoder.encode(dto.getPassword())) // 시큐리티의 암호 생성방식
                                .password(encoder.encode(dto.getPassword()))
                                .build())
                .getId();
    }


    public User findById(Long userId) { // 유저 ID로 유저 객체을 찾기
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }

    public User findByEmail(String email) { // 유저 email 정보로 유저 객체를 찾기
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }
}
