package com.cos.blog.controller;

import com.cos.blog.config.auth.PrincipalDetail;
import com.cos.blog.model.Board;
import com.cos.blog.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping(value = {"", "/"})
    public String index(Model model,
                        @PageableDefault(size = 3, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        //첫페이지(메인) 화면 제공 시 등록된 게시글이 있을경우 데이터 리턴한다.
        Page<Board> boards = boardService.글목록(pageable);
        model.addAttribute("boards", boards);
        return "index"; // /WEB-INF/views/index.jsp call => !!!   @Controller -> viewResolver 작동
    }

    @GetMapping(value = "/board/saveForm")
    public String saveForm() {
        System.out.println("BoardController.saveForm");
        return "/board/saveForm";
    }

}