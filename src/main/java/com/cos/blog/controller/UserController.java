package com.cos.blog.controller;

import com.cos.blog.model.User;
import com.cos.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 1. 인증이 안된 사용자들이 출입할 수 있는 경로를 /auth/** 허용한다.
 * 2. 그냥 주소가 / 이면 index.jsp 허용.
 * 3. static 이하에 있는 /js/**, /css/**, /image/** 허용.
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 고객이 회원가입을 버튼을 클릭했을경우 회원가입 폼을 제공한다.
     *
     * @return /WEB-INF/views/user/joinForm.jsp file return
     */
    @GetMapping(value = "/auth/joinForm")
    public String joinForm() {
        return "user/joinForm";
    }

    /**
     * 회원가입한 고객이 로그인 버튼을 클릭했을경우 로그인폼을 제공한다.
     *
     * @return /WEB-INF/views/user/loginForm.jsp file return
     */
    @GetMapping(value = "/auth/loginForm")
    public String loginForm() {
        return "user/loginForm";
    }


    @GetMapping(value = "/user/updateForm")
    public String updateForm() {
        System.out.println("UserController.updateForm");
        return "/user/updateForm";
    }


    /**
     * 회원정보 수정을 클릭했을경우 회원정보 수정 폼을 제공한다.
     * @return /WEB-INF/views/user/updateForm.jsp file return
     */
    /*
    @GetMapping(value = "/user/{id}")
    public String updateForm(Model model, @PathVariable("id") Integer id){
        model.addAttribute("user",userService.회원조회(id));
        return "user/updateForm";
    }
    */

}
