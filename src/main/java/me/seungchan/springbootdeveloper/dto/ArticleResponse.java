package me.seungchan.springbootdeveloper.dto;

import lombok.Getter;


// 응답을 위한 DTO를 먼저 작성
@Getter
public class ArticleResponse {

    private final String title;
    private final String content;

//    글은 제목과 내용 구성이므로
//    해당 필드를 가지는 클래스를 만든 다음
//    엔티티를 인수로 받는 생성자를 추가.
    public ArticleResponse(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
