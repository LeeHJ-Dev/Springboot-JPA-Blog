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
     * 사용자가 회원가입을 진행하는 경우 데이터 입력값을 User 객체로 입력받아 회원가입을 진행한다.
     * Path. Http://localhost:8000/auth/joinProc
     *
     * @param user 회원가입에 필요한 사용자 정보 객체
     * @return 회원가입이 정상적으로 진행되는 경우 리턴객체(ResponseDto Class : HttpStatus.OK.value(), 제네릭타입<Integer> 1 리턴)
     */
    @PostMapping(value = "/auth/joinProc")
    public ResponseDto<Integer> save(@RequestBody User user) {
        userService.회원가입(user);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }

    /**
     * 사용자가 인증 후 회원(본인)정보를 수정한다. 정상적으로 회원정보(DB)가 수정이 되면 Spring SecurityContext 사용자 정보가 변경되지 않기 때문에
     * 회원정보(세션)의 값도 직접 변경한다.
     * @param user 회원정보 수정이 필요한 사용자 객체
     * @return 회원수정이 정상적으로 진행되는 경우 리턴객체(ResponseDto Class : HttpStatus.OK.value(), 제네릭타입<Integer> 1 리턴)
     */
    @PutMapping(value = "/user")
    public ResponseDto<Integer> update(@RequestBody User user) {
        userService.회원수정(user);

        //여기서는 트랜잭션이 종료되기 때문에 DB에 값은 변경이 됐음
        //하지만, 세션값은 변경되지 않은 상태이기 때문에 우리가 직접 세션값을 변경해줄 것임.
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        //스프링시큐리티 세션에 사용자 인증(변경된 Authentication 주입)
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }
}
