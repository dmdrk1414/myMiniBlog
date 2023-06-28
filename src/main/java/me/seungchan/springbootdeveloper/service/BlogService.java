package me.seungchan.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.seungchan.springbootdeveloper.domain.Article;
import me.seungchan.springbootdeveloper.dto.AddArticleRequest;
import me.seungchan.springbootdeveloper.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor // final이 붙거나  @NotNull이 붙는 필드의 생성자 추가
@Service  // 빈으로 등록
public class BlogService {
    // TODO: 여기 삭제 하번 책 오류 확인
     @Autowired // 빈 주입
    private final BlogRepository blogRepository;


    // 블로그 글 추가 메서드
    public Article save(AddArticleRequest request) {
        // controller에서 받은 데이터를
        // blogRepository에 의해 DB로 저장한다.
        return blogRepository.save((request.toEntity()));
    }

    // 블로그 글 조회 메서드
    public List<Article> findAll() {
        return blogRepository.findAll();
    }

    // 데이터베이스에 저장되어 있는 글의 ID를 이용해 글을 조회한다.
    // 블로그 글 하나를 조회하는 메서드인 findById()메서드를 추가한다.
    public Article findById(long id) {
        return blogRepository.findById(id) // JPA에서 제공하는 메서드 findById()을 이용한다.
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id)); // 찾아서 없으면 예외처리.
    }
}
