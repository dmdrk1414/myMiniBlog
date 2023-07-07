package me.seungchan.springbootdeveloper.config.oauth;

import lombok.RequiredArgsConstructor;
import me.seungchan.springbootdeveloper.domain.User;
import me.seungchan.springbootdeveloper.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class OAuth2UserCustomService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    // 리소스 서버에서 보내주는 사용자 정보 (load)
    // 리소스 서버에서 보내주는 userRequest을 이용해 사용자의 정보를 보내준다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // OAuth2User의 사용자 객체는 식별자, 이름, 이메일, 프로필 사진 링크 등의 정보를 담고 있다.
        OAuth2User user = super.loadUser(userRequest); // ❶ 요청을 바탕으로 유저 정보를 담은 객체 반환
        // user 테이블의 데이터을 업데이트하는 방법
        saveOrUpdate(user);

        return user;
    }

    // ❷ 유저가 있으면 업데이트, 없으면 유저 생성
    // 리소스 서버에서 받은 정보를 이용해서 정보를 찾는다.
    private User saveOrUpdate(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");

        User user = userRepository.findByEmail(email)
                .map(entity -> entity.update(name))
                .orElse(User.builder()
                        .email(email)
                        .nickname(name)
                        .build());

        return userRepository.save(user);
    }
}

