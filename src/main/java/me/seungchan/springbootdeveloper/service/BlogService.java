package me.seungchan.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.seungchan.springbootdeveloper.domain.Article;
import me.seungchan.springbootdeveloper.dto.AddArticleRequest;
import me.seungchan.springbootdeveloper.dto.UpdateArticleRequest;
import me.seungchan.springbootdeveloper.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// lombok : @RequiredArgsConstructor
@RequiredArgsConstructor // final이 붙거나  @NotNull이 붙는 필드의 생성자 추가
@Service  // 해당 클래스를 빈으로 서블릿 컨테이너에 등록
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

    // 블로그 글의 ID를 받은 뒤
    // JPA에서 제공하는 deleteById()메서드를 이용해 데이터 베이스에서 데이터를 삭제합니다.
    public void delete(long id) {
        blogRepository.deleteById(id);
    }

    // 글수정 메서드
    // 리포지터리를 사용해 글을 수정하는 update() 메서드

    // @Transactional 매칭한 메서드를 하나의 트랜잭션으로 묶는 역할
    // update() 메서드는 엔티티의 필드 값이 바뀌면 중간에 에러가 발생해도 제대로 된
    // 값 수정을 보장하게 되었다.
    // 트랜잭션이란 데이터베이스의 데이터를 바꾸기 위해 묶은 작업의 단위
    @Transactional // 매칭한 메서드를 하나의 트랜잭션으로 묶는 역할
    public Article update(long id, UpdateArticleRequest request) {
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));

        article.update(request.getTitle(), request.getContent());

        return article;
    }
}
