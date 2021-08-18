package com.cos.blog.controller.api;

import com.cos.blog.config.auth.PrincipalDetail;
import com.cos.blog.dto.ReplySaveRequestDto;
import com.cos.blog.dto.ResponseDto;
import com.cos.blog.model.Board;
import com.cos.blog.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class BoardApiController {

    @Autowired
    private BoardService boardService;

    /**
     * 사용자 인증 및 글쓰기 작성을 진행하여 게시글을 등록하는 경우
     *
     * @param board     등록하고자 하는 게시글 정보
     * @param principal Spring Security 사용자 인증 후 (SecurityContextHolder->SecurityContext->) Authentication 인증정보(사용자 정보 관리되고 있음)
     * @return 등록이 정상적으로 진행된 경우 리턴객체(ResponseDto Class : HttpStatus.OK.Value(), 제네릭타입<Integer> 1 리턴)
     * @PostMapping(Path : /api/board)
     * @AuthenticationPrincipal 어노테이션은 UserDetailsService Interface 상속받은 PrincipalDetailService 클래스가 사용자 정보를 조회하여,
     * UserDetails Interface 상속받은 PrincipalDetail 클래스에 사용자 정보를 등록(저장)한다. 즉, @AuthenticationPrincipal 어노테이션으은 Spring Security Session
     * 사용자정보를 PrincipalDetail 객체에 주입한다.
     */
    @PostMapping(value = "/api/board")
    public ResponseDto<Integer> saveTitle(@RequestBody Board board,
                                          @AuthenticationPrincipal PrincipalDetail principal) {
        boardService.글쓰기(board, principal.getUser());
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }

    /**
     * 게시글을 삭제하는 경우(본인이 작성한 게시글만 삭제가능)
     * 1. @DeleteMapping(Path : /api/board/{id})
     *
     * @param id 삭제하고자 하는 게시글 Id
     * @return 삭제가 정상적으로 진행된 경우 리턴객체(ResponseDto Class : HttpStatus.OK.value(), 제네릭타입<Integer> 1 리턴)
     */
    @DeleteMapping(value = "/api/board/{id}")
    public ResponseDto<Integer> deleteById(@PathVariable("id") Integer id) {
        boardService.글삭제(id);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }

    /**
     * 게시글을 수정하는 경우(본인이 작성한 게시글만 수정가능)
     * 1. @PutMapping(Path : /api/board/{게시글번호})
     * 2. @RequestBody : 프론트에서 데이터 값을 Key:Value 형식으로 데이터를 전달한다.
     *
     * @param id    수정하고자 하는 게시글Id
     * @param board 수정하고자 하는 게시글 정보
     * @return 수정이 정상적으로 진행된 경우 리턴객체(ResponseDto Class : HttpStatus.OK.value(), 제네릭타입<Integer> 1 리턴)
     */
    @PutMapping(value = "/api/board/{id}")
    public ResponseDto<Integer> update(@PathVariable("id") Integer id,
                                       @RequestBody Board board) {
        boardService.글수정하기(id, board);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }

    /**
     * 게시글의 댓글을 등록하는경우 ReplySaveRequestDto(DTO:Data Transfer Object)를 사용하여 댓글을 등록한다.
     *
     * @param replySaveRequestDto 게시글의 댓글정보.
     * @return 등록이 정상적으로 진행된 경우 리턴객체(ResponseDto Class : HttpStatus.OK.value(), 제네릭타입<Integer> 1 리턴)
     * @PostMapping(Path : /api/board/{게시글번호}/reply)
     * 1. ReplySaveRequestDto userId : 댓글을 작성하고자 하는 사용자의 userId(pk)
     * 2. ReplySaveRequestDto boardId : 댓글을 작성하고자 하는 게시글의 boardId(pk)
     * 3. ReplySaveRequestDto content : 작성자가 입력한 댓글내용
     * 게시글의 댓글 입력 시 네이티브쿼리를 이용하여 댓글객체(Reply Class. Table)에 직접 등록한다. userId, boardId는 Foreign Key 이다.
     */
    @PostMapping(value = "/api/board/{boardId}/reply")
    public ResponseDto<Integer> replySave(@RequestBody ReplySaveRequestDto replySaveRequestDto) {
        //boardService.댓글등록하기(replySaveRequestDto);
        boardService.댓글등록하기_네이티브(replySaveRequestDto);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }

    /**
     * 게시글의 댓글을 삭제하는 경우 댓글번호를 이용하여 댓글을 삭제한다.
     *
     * @param replyId 삭제하고자 하는 게시글의 번호
     * @return 삭제가 정상적으로 진행된 경우 리턴객체(ResponseDto Class : HttpStatus.OK.value(), 제네릭타입<Integer> 1 리턴)
     * @DeleteMapping(Path : /api/board/{게시글번호}/reply/{댓글번호})
     */
    //댓글삭제하기
    @DeleteMapping(value = "/api/board/{boardId}/reply/{replyId}")
    public ResponseDto<Integer> replyDelete(@PathVariable("replyId") Integer replyId) {
        boardService.댓글삭제하기(replyId);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }
}