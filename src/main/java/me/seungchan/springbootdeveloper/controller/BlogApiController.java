package me.seungchan.springbootdeveloper.controller;

import lombok.RequiredArgsConstructor;
import me.seungchan.springbootdeveloper.domain.Article;
import me.seungchan.springbootdeveloper.dto.AddArticleRequest;
import me.seungchan.springbootdeveloper.dto.ArticleResponse;
import me.seungchan.springbootdeveloper.dto.UpdateArticleRequest;
import me.seungchan.springbootdeveloper.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 컨트롤러 계층 설명
// 컨트롤러 메서드에는 URL 매핑 애너테이션
// @GetMapping, @PostMapping, @PutMapping, @DeleteMapping 을 사용

@RequiredArgsConstructor // final 이 붙거나 @NotNull 이 붙은 필드의 생성자를 자동 생성해주는 룸복 어노테이션

// @RestController
// HTTP 응답으로 객체 데이터를 JSON 형식으로 반환
@RestController // HTTP Response Body에 객체 데이터를 JSON 형식으로 반환하는 컨트롤러
public class BlogApiController {
    // TODO : 빈주신 삭제
    @Autowired
    private final BlogService blogService;

    // HTTP 메서드가 POST일 때 전달 받은 URL와 동일하면 메서드로 매핑
    @PostMapping("/api/articles")
    // 요청 본문 값 매핑
    // @RequestBody 애너테이션은 HTTP를 요청할 때 응답에 해당하는 값을
    // @RequestBody 애너테이션이 붙은 대상 객체인 AddArticleRequest에 매핑한다.
    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request) {
        Article savedArticle = blogService.save(request);

        // 요청한 자원이 성공적으로 생성되었으며 저장된 블로그 글 정보를 응답 객체에 담아 전송
        // 요청한 정보를 로직처리를 하여 리턴을 한다.
        return ResponseEntity.status(HttpStatus.CREATED) // 응답 코드로 201, Created를 응답하고
                .body(savedArticle); // 테이블에 저장된 객체를 반환
    }

    // HTTP 메서드가 GET일 때 전달 받는 URL와 동일하면 메서드로 매핑
    @GetMapping("/api/articles") // 조회한뒤 반환
    public ResponseEntity<List<ArticleResponse>> findAllArticles() {
        List<ArticleResponse> articles = blogService.findAll()
                .stream()

                // blogService에서 찾아온 Article의 하나하나가 파라미터로 넘어간다.
                .map(ArticleResponse::new)
                .toList();

        return ResponseEntity.ok()
                .body(articles);
    }

    // /api/articles/{id} Get 요청이 오면 블로그 글을 조회하기 위해 매핑할 메서드
    // URL 경로에서 값 추출
    @GetMapping("/api/articles/{id}")
    // @PathVariable 매너테이션은 URL에서 값을 가져오는 애너테이션
    // /api/articles/3 GET 요청을 받으면 id에 3이 들어온다.
    public ResponseEntity<ArticleResponse> findArticle(@PathVariable long id) {
        Article article = blogService.findById(id);

        return ResponseEntity.ok()
                .body(new ArticleResponse(article));
    }

    // /api/articles/{id} DELETE 요청이 오면
    // 글을 삭제하기 위한 findArticles() 메서드를 작성
    @DeleteMapping("/api/articles/{id}")
    // @PathVariable 매너테이션은 URL에서 값을 가져오는 애너테이션
    // /api/articles/3 GET 요청을 받으면 id에 3이 들어온다.
    public ResponseEntity<Void> deleteArticle(@PathVariable long id) {
        blogService.delete(id);

        return ResponseEntity.ok()
                .build();
    }

    // /api/articles/{id} PUT 요청이 오면 글을 수정하기 위한 updateArticle() 메서드를 작성.
    @PutMapping("/api/articles/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable long id, @RequestBody UpdateArticleRequest request) {
        Article updateArticle = blogService.update(id, request);

        return ResponseEntity.ok()
                .body(updateArticle);
    }
}
