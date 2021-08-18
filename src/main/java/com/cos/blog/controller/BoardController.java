package com.cos.blog.controller;

import com.cos.blog.model.Board;
import com.cos.blog.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
     * 사용자가 인증 후 게시글을 작성(글쓰기)을 하는 경우 화면 주소를 제공한다.
     * ### 화면(폼) 경로제공 ###
     * Path. Http://localhost:8000/board/saveForm
     *
     * @return /WEB-INF/views/board/saveForm.jsp file return
     */
    @GetMapping(value = "/board/saveForm")
    public String saveForm() {
        return "/board/saveForm";
    }

    /**
     * 사용자 인증 후 게시글(본인작성)을 수정하는 경우 게시글 수정 폼을 제공한다.
     * ## 화면(폼) 경로제공 ###
     * Path. Http://localhost:8000/board/updateForm
     *
     * @param model @Controller viewResolver Model 값 리턴. ( key:value -> "board":Board Class )*
     * @param id    게시글(Id)
     * @return /WEB-INF/views/board/updateForm.jsp file return
     */
    @GetMapping(value = "/board/{id}/updateForm")
    public String updateForm(Model model, @PathVariable("id") Integer id) {
        model.addAttribute("board", boardService.글상세보기(id));
        return "/board/updateForm";
    }

    /**
     * 사용자가 게시글을 보기위해서 게시글Id를 선택하면 해당 게시글을 정보와 게시글 상세보기 폼을 제공한다.
     * Path. Http://localhost:8000/board/{게시글Id}
     *
     * @param model @Controller viewResolver Model 값 (게시글 상세정보를 보기위해서 리턴하기 위한 모델 객체)
     * @param id    게시글Id
     * @return 게시글 정보와 /WEB-INF/views/board/detail.jsp file return
     */
    @GetMapping(value = "/board/{id}")
    public String findById(Model model, @PathVariable("id") Integer id) {
        model.addAttribute("board", boardService.글상세보기(id));
        return "/board/detail";
    }
}