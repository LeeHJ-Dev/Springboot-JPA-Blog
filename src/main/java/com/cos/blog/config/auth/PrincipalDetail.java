package com.cos.blog.config.auth;

import com.cos.blog.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

//스프링 시큐리티가 로그인 요청을 가로채서 로그인을 진행하고 완료가 되면 UserDetails 타입의 오브젝트를
//스프링 시큐리티의 고유한 세션저장소에 저장을 해준다.

public class PrincipalDetail implements UserDetails {

    private User user;      // 콤포지션

    public PrincipalDetail(User user) {
        System.out.println("PrincipalDetail.PrincipalDetail 1111 ");
        this.user = user;
        System.out.println("PrincipalDetail.PrincipalDetail 2222 ");
    }

    @Override
    public String getPassword() {
        System.out.println("PrincipalDetail.getPassword");
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        System.out.println("PrincipalDetail.getUsername");
        return user.getUsername();
    }

    // 계정이 만료되지 않았는지 리턴한다. (true: 만료안됨)
    @Override
    public boolean isAccountNonExpired() {
        System.out.println("PrincipalDetail.isAccountNonExpired");
        return true;
    }

    // 계정이 잠겨있지 않았는지 리턴한다.(true: 잠기지 않음)
    @Override
    public boolean isAccountNonLocked() {
        System.out.println("PrincipalDetail.isAccountNonLocked");
        return true;
    }

    //비밀번호가 만료되지 않았는지 리턴한다.(true: 만료안됨)
    @Override
    public boolean isCredentialsNonExpired() {
        System.out.println("PrincipalDetail.isCredentialsNonExpired");
        return true;
    }

    //계정이 활성화(사용가능)인지 리턴한다.(true:활성화)
    @Override
    public boolean isEnabled() {
        System.out.println("PrincipalDetail.isEnabled");
        return true;
    }

    //계정이 갖고 있는 권한 목록을 리턴한다.
    //권한이 여러개 있을 수 있어서 르푸를 돌아야 하는데 우리는 한개만
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        System.out.println("PrincipalDetail.getAuthorities");
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
