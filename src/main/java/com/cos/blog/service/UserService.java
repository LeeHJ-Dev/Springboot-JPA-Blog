package com.cos.blog.service;

import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service    //스프링이 컴포넌트 스캔을 통해서 Bean에 등록해준다.(IOC)
//@RequiredArgsConstructor
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Transactional(readOnly = false)
    public void 회원가입(User user) {

        String rawPassword = user.getPassword();    //1234
        String encPassword = encoder.encode(rawPassword);

        System.out.println("rawPassword = " + rawPassword);     //암호화가 안된것.
        System.out.println("encPassword = " + encPassword);     //암호화가 된것.(해쉬)

        user.setRole(RoleType.USER);
        user.setPassword(encPassword);

        userRepository.save(user);
    }

    /*
    @Transactional(readOnly = false)
    public User 회원로그인(User user) {
        //회원이름체크
        if(!StringUtils.hasText(user.getUsername())){

        }

        //비밀번호체크
        if(!StringUtils.hasText(user.getPassword())){

        }

        User findUser = userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
        if(findUser == null){

        }
        return findUser;
    }
    */
}