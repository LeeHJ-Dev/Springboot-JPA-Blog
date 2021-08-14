package com.cos.blog.controller.api;

import com.cos.blog.config.auth.PrincipalDetail;
import com.cos.blog.dto.ResponseDto;
import com.cos.blog.model.Board;
import com.cos.blog.service.BoardService;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class BoardApiController {

    @Autowired
    private BoardService boardService;

    /**
     * 화면에서 글쓰기 작성 후 완료를 클릭했을경우 @RequestBody 객체(http, post) 를 이용해서 글쓰기 객체를 호출한다.
     *
     * @param board     title, contents
     * @param principal @AuthenticationPrincipal 어노테이션은 UserDetailsService Interface 상속받은 PrincipalDetailService
     *                  클래스가 사용자 정보를 조회하여 UserDetails Interface 상속받은 PrincipalDetail 클래스에 사용자 정보를 저장한다.
     *                  즉, @AuthenticationPrincipal 어노테이션은 Spring Security Session 사용자정보를
     *                  PrincipalDetail 객체의 사용자 정보를 주입받는다.
     * @return
     */
    @PostMapping(value = "/api/board")
    public ResponseDto<Integer> saveTitle(@RequestBody Board board,
                                          @AuthenticationPrincipal PrincipalDetail principal) {
        boardService.글쓰기(board, principal.getUser());
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }

    @DeleteMapping(value = "/api/board/{id}")
    public ResponseDto<Integer> deleteById(@PathVariable("id") Integer id) {
        boardService.글삭제(id);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }

    @PutMapping(value = "/api/board/{id}")
    public ResponseDto<Integer> update(@PathVariable("id") Integer id,
                                       @RequestBody Board board) {
        boardService.글수정하기(id, board);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }

}




















