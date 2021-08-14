package com.cos.blog.controller.api;

import com.cos.blog.dto.ResponseDto;
import com.cos.blog.model.User;
import com.cos.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserApiController {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder encode;

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * 1. 경로가 /auth/** , 로그인 안한 사람들도 접근이 가능하다.
     * ex. http://localhost:8000/auth/joinProc
     *
     * @param user
     * @return
     */
    @PostMapping(value = "/auth/joinProc")
    public ResponseDto<Integer> save(@RequestBody User user) {
        userService.회원가입(user);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }

    /**
     * 회원정보의 패스워드, 이메일 정보를 수정한다.
     *
     * @param user
     * @return
     */
    @PutMapping(value = "/user")
    public ResponseDto<Integer> update(@RequestBody User user) {
        System.out.println("UserApiController.update");
        userService.회원수정(user);

        //여기서는 트랜잭션이 종료되기 때문에 DB에 값은 변경이 됐음
        //하지만, 세션값은 변경되지 않은 상태이기 때문에 우리가 직접 세션값을 변경해줄 것임.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }











    /*
    @PostMapping(value = "/api/user/login" )
    public ResponseDto<Integer> login(@RequestBody User user, HttpSession session){
        User principal = userService.회원로그인(user);   //접근주체

        if(principal != null){
            //세션생성
            session.setAttribute("principal", principal);
        }
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }
    */
}
