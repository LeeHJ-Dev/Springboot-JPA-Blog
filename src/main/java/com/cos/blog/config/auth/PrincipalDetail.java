package com.cos.blog.config.auth;

import com.cos.blog.model.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

//스프링 시큐리티가 로그인 요청을 가로채서 로그인을 진행하고 완료가 되면 UserDetails 타입의 오브젝트를
//스프링 시큐리티의 고유한 세션저장소에 저장을 해준다.

/**
 * Spring Security 에서 사용자의 정보를 담는 인터페이스 : UserDetails Interface Class
 * UserDetails Interface 상속받은 PrincipalDetail 에서 Override Method 구현한다.
 * 이 인테페이스를 구현하게 되면 Spring Security 에서 구현한 클래스를 사용자 정보로 인식하고 인증 작업을 한다.
 * -> UserDetails 인터페이스는 VO(Value Object) 역할을 한다고 보면된다.
 * 사용자의 정보를 모두 담아두는 클래스를 구현하면된다. 오버라이드 되는 메소드만 Spring Security 에서 알아서 이용하기 때문에
 * 따로 클래스를 만들지 않고 멤버변수를 추가해서 같이 사용해도 무방하다.
 * <p>
 * 1.
 */

@Getter
public class PrincipalDetail implements UserDetails {

    // User 정보를 선언하여 Authentication 에서 사용할 것이므로 따로 어노테이션을 해주지 않는다.
    private User user;      // 콤포지션

    // UserDetailService Interface 상속받은 PrincipalDetailService 에서 조회된 User 정보를 받는다.
    public PrincipalDetail(User user) {
        this.user = user;
    }

    // User 계정의 비밀번호를 리턴한다.(HashValue)
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // User 계정의 이름을 리턴한다.
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // 계정이 만료되지 않았는지 리턴한다. (true: 만료안됨)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정이 잠겨있지 않았는지 리턴한다.(true: 잠기지 않음)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //비밀번호가 만료되지 않았는지 리턴한다.(true: 만료안됨)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //계정이 활성화(사용가능)인지 리턴한다.(true:활성화)
    @Override
    public boolean isEnabled() {
        return true;
    }

    //계정이 갖고 있는 권한 목록을 리턴한다.
    //권한이 여러개 있을 수 있어서 르프를 돌아야 하는데 우리는 한개만
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collectors = new ArrayList<>();
        /*
        collectors.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return "ROLE_" + user.getRole(); // ROLE 필수. ROLE_USER
            }
        })
        */

        collectors.add(() -> {
            return "ROLE_" + user.getRole();
        });
        return collectors;
    }
}
