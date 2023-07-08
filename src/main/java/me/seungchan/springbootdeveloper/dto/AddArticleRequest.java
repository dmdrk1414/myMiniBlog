package me.seungchan.springbootdeveloper.dto;

import lombok.*;
import me.seungchan.springbootdeveloper.domain.Article;

// dto : 패키지 (data transfer object) 계층끼리 데이터를 교환하기 위해 사용하는 객체
// 단순하게 데이터를 옮기기 위해 사용하는 전달자 역할을 하는 객체이기 때문에 별도의 비즈니스 로직을 포함하지 않는다.
// 컨트롤러에서 요청 본문을 받을 객체인 AddArticleRequest.java 파일을 생성

@NoArgsConstructor // 기본 생성자 추가.
@AllArgsConstructor // 모든 필드 값을 파라미터로 받는 생성자 추가.
@Getter
public class AddArticleRequest {

    private String title;
    private String content;

    // builder 패턴을 사용해 DTO를 엔티티로 사용 가능하다
    // toEntity()는 빌더 패턴을 사용해 DTO를 엔티티로 만들어주는 메서드
    // 블로그에 글을 추가할 때 저장할 엔티티로 변환하는 용도로 사용
    public Article toEntity(String author){ // 생성자를 사용해 객체 생성
        return Article.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();
    }
}
