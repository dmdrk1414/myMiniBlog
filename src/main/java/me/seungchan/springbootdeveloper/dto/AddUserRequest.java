package me.seungchan.springbootdeveloper.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// 사용자의 정보를 담고 있는 객체를 작성
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AddUserRequest {
    private String email;
    private String password;
    private String auth;
}
