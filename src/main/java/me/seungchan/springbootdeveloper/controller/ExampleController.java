package me.seungchan.springbootdeveloper.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.List;

// RestController 와는 다르다.
@Controller // 컨트롤러라는 것을 명시적으로 표시, 뷰의 이름을 반환한다는 뜻이다.
public class ExampleController {

    @GetMapping("/thymeleaf/example")
    public String thymeleafExample(Model model) { // 뷰로 데이터를 넘겨주는 모델 객체
        Person examplePerson = new Person(); // 바로 밑에 Person 클래스 있다.
        examplePerson.setId(1L);
        examplePerson.setName("홍길동");
        examplePerson.setAge(11);
        examplePerson.setHobbies(List.of("운동", "독서"));

        model.addAttribute("person", examplePerson);
        model.addAttribute("today", LocalDate.now());

        return "example";
    }
}

@Getter
@Setter
class Person{
    private Long id;
    private String name;
    private int age;
    private List<String> hobbies;
}
