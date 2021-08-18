package com.cos.blog.config.auth;

import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 사용자의 정보를 담을 객체(UserDetails 인터페이스를 상속받은 PrincipalDetail)를 생성하였으니, UserRepository Class 이용해서
 * DB 에서 사용자의 정보를 불러오는 중요한 메소드를 구현해야한다.
 * UserDetailsService Interface 선언된 loadUserByName() 메소드를 오버라이드 구현해야한다.
 * 사용자의 정보를 조회하여 정보의 유/무에 따라 예외 or 사용자 정보를 리턴한다.
 */
@Service    //Bean 등록
public class PrincipalDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User principal = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    return new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다. : " + username);
                });
        return new PrincipalDetail(principal);
    }
}
