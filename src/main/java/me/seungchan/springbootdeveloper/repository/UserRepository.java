package me.seungchan.springbootdeveloper.repository;

import me.seungchan.springbootdeveloper.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 이메일은 사용자를 식별할 수 있다.
    // 사용자 이름으로 봐도 된다.
    // 따라서 사용자 정보를 가져오기 위해서는 스프링 시큐리티가 이메일을 전달 받아야한다.
    // 스프링 데이터 JPA는 메서드 규칙에 맞춰 메서드를 선언하면 이름을 분석해 자동으로 쿼리를 생성한다.
    // findByEmail() 메서드는 실제 데이터베이스에 회원 정보를 요청할 때 다음 쿼리를 실행합니다.

    Optional<User> findByEmail(String email); // email로 사용자 정보를 가져온다.
    // FROM users
    // WHERE email = #{email}   의 쿼리 메서드의 명명 규칙이 나온다.


}
