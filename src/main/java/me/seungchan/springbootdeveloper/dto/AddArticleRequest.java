package me.seungchan.springbootdeveloper.dto;

import lombok.*;
import me.seungchan.springbootdeveloper.domain.Article;

// dto : 패키지 (data transfer object) 계층끼리 데이터를 교환하기 위해 사용하는 객체
// 단순하게 데이터를 옮기기 위해 사용하는 전달자 역할을 하는 객체이기 때문에 별도의 비즈니스 로직을 포함하지 않는다.

@NoArgsConstructor // 기본 생성자 추가.
@AllArgsConstructor // 모든 필드 값을 파라미터로 받는 생성자 추가.
@Getter
public class AddArticleRequest {

    private String title;
    private String content;

    // builder 패턴을 사용해 DTO를 엔티티로 사용 가능하다
    public Article toEntity(){ // 생성자를 사용해 객체 생성
        return Article.builder()
                .title(title)
                .content(content)
                .build();
    }
}
