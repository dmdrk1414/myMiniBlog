package me.seungchan.springbootdeveloper.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 블로그 글 수정 요청을 받을 DTO를 작성

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateArticleRequest {
    // 수정 요청을 받는 DTO이다.
    private String title;
    private String content;
}
