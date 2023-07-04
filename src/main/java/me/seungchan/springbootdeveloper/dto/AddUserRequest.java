package me.seungchan.springbootdeveloper.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

// 사용자의 정보를 담고 있는 객체를 작성
@NoArgsConstructor
@Getter
public class AddUserRequest {
    private String email;
    private String password;
    private String auth;
}
