package com.cos.blog.controller;

import com.cos.blog.config.auth.PrincipalDetail;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BoardController {

    @GetMapping(value = {"", "/"})
    public String index(@AuthenticationPrincipal PrincipalDetail principal) {
        System.out.println("principalDetail = " + principal.getUsername());
        // /WEB-INF/views/index.jsp call
        return "index";
    }
}
