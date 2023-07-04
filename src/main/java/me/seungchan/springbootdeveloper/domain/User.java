package me.seungchan.springbootdeveloper.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

// UserDetails 클래스를 상혹하는 User 클래스를 만든다.
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class User implements UserDetails { // UserDetails를 상속받아 인증 객체로 사용
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    // user의 unique
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Builder
    public User(String email, String password, String auth) {
        this.email = email;
        this.password = password;
    }

    // 권한 관련 ---------------------------------------a

    // 사용자가 가지고 있는 권한의 목록을 반환합니다.
    // 현재 예제 코드에서는 사용자 이외의 권한이 없기 때문에 user 권한만 담아 반환한다.
    @Override // 권한 반환
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 사용자 이외의 권한이 없기 때문에 user 유저 권한만 담아 반환
        return List.of(new SimpleGrantedAuthority("user"));
    }

    // 사용자를 식별할 수 있는 사용자 이름을 반환합니다.
    // 이때 사용되는 사용자 이름은 반드시 고유해야 합니다.
    // 현재 예제 코드는 유니크 속성이 적용된 이메일을 반환합니다
    @Override // 사용자의 id를 반환 (고유한 값)
    public String getUsername() {
        return email;
    }

    // 사용자의 비밀번호를 반환합니다.
    // 이때 저장되어 있는 비밀번호는 암호화해서 저장한다.
    @Override // 사용자의 패스워드를 반환
    public String getPassword() {
        return password;
    }

    // 계정이 만료되었는지 확인하는 메서드입니다.
    // 만약 만료되지 않은때는
    @Override // 계정 만료 여부 반환
    public boolean isAccountNonExpired() {
        // 만료되었는지 확인하는 로직
        return true;
    }

    // 계정이 잠금되었는지 확인하는 메서드입니다.
    // 만약 잠금되지 않은 때는 true를 반환합니다.
    @Override // 계정 잠금 여부 반환
    public boolean isAccountNonLocked() {
        // 계정 잠금되었는지 확인하는 로직
        return true; // true -> 잠금되지 않았음
    }

    // 비밀번호가 만료되었는지 확인하는 메서드입니다.
    // 만약 만료되지 않은 때는 true를 반환합니다.
    @Override // 패스워드의 만료 여부 반환
    public boolean isCredentialsNonExpired() {
        // 패스워드가 만료되었는지 확인하는 로직
        return true; // ture -> 만료되지 않았음
    }

    // 계정이 사용 가능한지 확인하는 메서드입니다.
    // 만약 사용 가능하다면 true을 반환한다.
    @Override // 계정 사용 가능 여부 반환
    public boolean isEnabled() {
        // 계정이 사용 가능한지 확인하는 로직
        return true; // true -> 사용 가능
    }
}
