package me.seungchan.springbootdeveloper.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.seungchan.springbootdeveloper.domain.Article;
import me.seungchan.springbootdeveloper.dto.AddArticleRequest;
import me.seungchan.springbootdeveloper.dto.UpdateArticleRequest;
import me.seungchan.springbootdeveloper.repository.BlogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

// MockMVC 메서드 설명
// perform() : 메서드는 요청을 전송하는 역할을 하는 메서드
//              반환은 ResultActions 객체를 받으며
//              ResultActions 객체는 반환값을 검증하고 확인한는 andExpect() 메서드를 제공

// accept() : 메서드는 요청을 보낼 때 무슨 타입으로 응답을 받을지 결정하는 메서드
//              JSON, XML 등 다양한 타입이 있지만, JSON을 받는다고 명시해둔다.

// jsonPath("$[0].${필드명}) : JSON 응답값의 값을 가져오는 역할을 하는 메서드
//                           0번째 배열에 들어있는 객체의 id, name값을 가져온다
// ------------------------------------------------------------------

// 메인 애플리케이션 클래스에 추가하는 애너테이션인 @SpringBootApplication이 있는 클래스 찾고
// 클래스에 포함되어 있는 빈을 찾은 다음, 테스트용 애플리케이션 컨텍스트라는 것을 만든다.
@SpringBootTest // 테스트용 애플리케이션 컨텍스트

// @AutoConfigureMockMvc는 MockMvc를 생성, 자동으로 구성하는 애너테이션
// MockMvc는 어플리케이션을 서버에 배포하지 하지 않고 테스트용 MVC 환경을 만들어 요청 및 전송, 응갇기능을 제공하는 유틸리티 클래스
// 컨트롤러를 테스트를 할때 사용되는 클래스
@AutoConfigureMockMvc // MockMvc 생성
class BlogApiControllerTest {

    // MockMvc 생성, MockMvc는 애플리케이션을 서버에 배포하지 않고, 테스트용 MVB 환경을 만들어 요청 및 전송, 응답 기능을 제공하는것
    // 컨트롤러를 테스트할 때 사용되는 클래스
    @Autowired
    protected MockMvc mockMvc;

    // ObjectMapper 클래스 - 직렬화, 역직렬화 할때 사용
    // 자바 객체를 JSON 데이터로 변환 OR JSON 데이터를 자바 객체로 변환
    // 직렬화 : 자바 시스템 내부에서 사용하는 객체를 외부에서 사용하도록 데이터를 변환하는 작업
    @Autowired
    protected ObjectMapper objectMapper; // 직렬화, 역직렬화를 위한 클래스

    @Autowired
    private WebApplicationContext context;

    @Autowired
    BlogRepository blogRepository;

    @BeforeEach // 테스트 실행 전 실행하는 메서드
    public void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context) // MockMVB 설정
                .build();
        blogRepository.deleteAll();
    }

    @DisplayName("addArticle: 블로그 글추가에 성공한다. ")
    @Test
    public void addArticle() throws Exception {
        // given
        // 블로그 글추가에 필요한 요청 객체 만들기
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";
        final AddArticleRequest userRequest = new AddArticleRequest(title, content);

        // 객체 JSON 으로 직렬화
        //
        final String requestBody = objectMapper.writeValueAsString(userRequest); // 객체의 직렬화

        // when
        // 성정한 내용을 바탕으로 요청 전송
        // 메서드설명 윗 주석 참고
        ResultActions result = mockMvc.perform(post(url) // 블로그 글 추가에 필요한 요청 객체를 만든다.
                .contentType(MediaType.APPLICATION_JSON_VALUE) // 쵸청 타입은 JSON입니다.
                .content(requestBody)); // given 절에서 미리 만들어둔 객체를 요청 본문으로 함께 보냅니다.

        // then
        result.andExpect(status().isCreated());

        List<Article> articles = blogRepository.findAll();

        assertThat(articles.size()).isEqualTo(1); // 크기가 1인지 검증
        assertThat(articles.get(0).getTitle()).isEqualTo(title); // article의 제목이 title과 같은지 확인
        assertThat(articles.get(0).getContent()).isEqualTo(content); // article의 내용이 content과 같은지 확인
    }


    @DisplayName("findAllArticles: 블로그 글 목록 조회에 성공한다.")
    @Test
    public void findAllArticles() throws Exception{
        //given
        // 블로그 글 관련 정보들을 저장
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";

        blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());

        //when
        // 목록 조회 API 호출
        final ResultActions resultActions = mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON));

        //then
        // 0번 인덱스 요소중
        // content와 title이 저장된 값과 같은지 확인
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value(content))
                .andExpect(jsonPath("$[0].title").value(title));
    }

    @DisplayName("findArticle: 블로그 글 조회에 성공한다. 특정 ID을 이용하여")
    @Test
    public void findArticle() throws Exception{
        // given
        // 블로그 글을 저장합니다.
        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";

        Article savedArticle = blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());

        // when
        // 저장한 블로그 글의 id값으로 API를 호출합니다.
        final ResultActions resultActions = mockMvc.perform(get(url, savedArticle.getId()));

        // then
        // 응답 코드가 200 OK 이고, 반환받은 content와 title이 저장된 값과 같은지 확인한다.
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(content))
                .andExpect(jsonPath("$.title").value(title));

    }

    @DisplayName("deleteArticle: 블로그 글 삭제에 성공한다.")
    @Test
    public void deleteArticleTest() throws Exception{
        // given
        // 블로그 글 삭제에 성공한다.
        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";

        Article savedArticle = blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build()
        );

        // when
        // 저장한 블로그 글의 id값으로 API를 호출한다.
        // 여기서 삭제를 한후
        mockMvc.perform(delete(url, savedArticle.getId()))
                .andExpect(status().isOk()); // 여기서 삭제를 한수


        // then
        // 응답 코드가 200 OK이고,
        // 블로그 글 리스트를 전체 조회해 조회한 배열의 크기가 0인지 확인.
        List<Article> articles = blogRepository.findAll(); // 전체 초회를 해서 반환한 배열

        assertThat(articles).isEmpty(); // 배열이 비었으면
    }

    @DisplayName("updateArticle: 블로그 글 수정에 성공한다.")
    @Test
    public void updateArticleTest() throws Exception{
        // given
        // 블로그 글을 저장하고
        // 블로그 글 수정에 필요한 요청 객체를 만든다.
        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";

        Article savedArticle = blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build()
        );

        // 새로운 데이터
        final String newTitle = "new Title";
        final String newContent = "new content";

        UpdateArticleRequest request = new UpdateArticleRequest(newTitle, newContent);

        // when
        // UPDATE API로 수정 요청을 보낸다.
        // 이때 요청 타입은 JSON이며
        // given절에서 미리 만들어둔 객체를 요청 본문으로 함께 보냅니다.
        ResultActions result = mockMvc.perform(put(url, savedArticle.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request))); // java 객체를 JSON 형식으로 변환, 수정요청

        // then
        // 응답코드가 200 OK인지 확인
        // 블로그 글 id로 조회한 후에 값이 수정되었는지 확인
        result.andExpect(status().isOk()); // 변경된 것이 잘되었는지 확인

        Article article = blogRepository.findById(savedArticle.getId()).get(); // 현재 기사의 id을 가지고 Article 객체를 얻는다.

        assertThat(article.getTitle()).isEqualTo(newTitle);
        assertThat(article.getContent()).isEqualTo(newContent);

    }
}