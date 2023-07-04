package me.seungchan.springbootdeveloper.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity

// 롬복 : @Getter
// getId(), getTitle() 같이 필드의 값을 가져오는 게터 메서드
@Getter // Getter 메서드 대안

// lombok: @NoArgsConstructor
// Protected 기본 생성자 생성
@NoArgsConstructor (access = AccessLevel.PROTECTED) // 기본 생성자 대안
public class Article {
    @Id // id 필드를 기본키로 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본키 자동으로 1씩 증가
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "title", nullable = false) // 'title'이라는 not null 컴럼과 매핑
    private String title;

    @Column(name = "content", nullable = false)
    private String content;


    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // @Builder
    // 롬복에서 지원하는 애너테이션
    // 생성자 위에 입력하면 빌더 패턴 방식으로 객체를 생가능하다.
    // 빌더 패턴은 어느 필들에 어떤 값이 들어가는지 명시적으로 파악
    @Builder // 빌더 패턴으로 객체 생성
    public Article(String title, String content) {
        this.title = title;
        this.content = content;

        // 기본적인 생성자
        // new Article("abc", "def");

        // 빌더 패턴을 사용
        // Article.builder()
        //      .title("abc")
        //      .content("def")
        //      .build();
    }

    // 엔티티의 데이터를 수정할 수 있는 메서드
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
