package com.cos.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 1. 인증이 안된 사용자들이 출입할 수 있는 경로를 /auth/** 허용한다.
 * 2. 그냥 주소가 / 이면 index.jsp 허용.
 * 3. static 이하에 있는 /js/**, /css/**, /image/** 허용.
 */
@Controller
public class UserController {
    /**
     * 고객이 회원가입을 버튼을 클릭했을경우 회원가입 폼을 제공한다.
     *
     * @return 회원가입폼
     */
    @GetMapping(value = "/auth/joinForm")
    public String joinForm() {
        return "user/joinForm"; // /WEB-INF/views/joinForm.jsp
    }

    /**
     * 회원가입한 고객이 로그인 버튼을 클릭했을경우 로그인폼을 제공한다.
     *
     * @return 로그인폼
     */
    @GetMapping(value = "/auth/loginForm")
    public String loginForm() {
        return "user/loginForm"; // /WEB-INF/views/loginForm.jsp
    }
}
