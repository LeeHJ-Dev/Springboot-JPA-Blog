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
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller // viewResolver Call.
public class BoardController {

    @Autowired
    private BoardService boardService;

    /**
     * 메인페이지. 게시글목록을 조회하며 게시글이 있을경우 Page<T> 데이터를 리턴한다.
     *
     * @param model
     * @param pageable 게시글목록을 조회시 (기본)페이징 전략
     * @return /WEB-INF/views/index.jsp file name Return
     */
    @GetMapping(value = {"", "/"})
    public String index(Model model,
                        @PageableDefault(size = 3, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        //첫페이지(메인) 화면 제공 시 등록된 게시글이 있을경우 데이터 리턴한다.
        Page<Board> boards = boardService.글목록(pageable);
        model.addAttribute("boards", boards);
        return "index";
    }

    /**
     * 화면의 글쓰기 메뉴를 클릭했을경우 (http://localhost:8000/board/saveForm 호출)
     *
     * @return WEB-INF/view/board/saveForm.jsp file name return;
     */
    @GetMapping(value = "/board/saveForm")
    public String saveForm() {
        return "/board/saveForm";
    }

    @GetMapping(value = "/board/{id}/updateForm")
    public String updateForm(Model model,
                             @PathVariable("id") Integer id) {
        model.addAttribute("board", boardService.글상세보기(id));
        return "/board/updateForm";
    }


    /**
     * 게시글의 상세목록을 조회하는경우
     *
     * @return WEB-INF/view/board/saveDe.jsp file name return;
     */
    @GetMapping(value = "/board/{id}")
    public String findById(Model model, @PathVariable("id") Integer id) {
        model.addAttribute("board", boardService.글상세보기(id));
        return "/board/detail";
    }


}
















