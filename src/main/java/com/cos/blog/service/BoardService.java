package com.cos.blog.service;

import com.cos.blog.dto.ReplySaveRequestDto;
import com.cos.blog.model.Board;
import com.cos.blog.model.Reply;
import com.cos.blog.model.User;
import com.cos.blog.repository.BoardRepository;
import com.cos.blog.repository.ReplyRepository;
import com.cos.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private UserRepository userRepository;

    /**
     * 사용자가 최초 글쓰기를 작성하면 보드객체, 사용자객체(시큐리트 세션 유저정보)를 입력받아 보드객체를 저장한다.
     * 사용자의 정보가 필요한 이유는 보드객체의 사용자정보를 입력해야 fk 입력이 필요하다.
     *
     * @param board
     * @param user
     */
    @Transactional(readOnly = false)
    public void 글쓰기(Board board, User user) {
        board.setCount(0);
        board.setUser(user);
        Board saveBoard = boardRepository.save(board);
    }

    /**
     * 메인페이지 호출 시 게시글 목록을 조회한다.
     *
     * @param pageable 게시글 목록을 페이징 처리
     * @return 페이징처리된 게시글 목록을 리턴한다.
     */
    @Transactional(readOnly = true)
    public Page<Board> 글목록(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    /**
     * 메인페이지에 게시글 중 특정 게시글을 선택하여 게시글의 상세정보를 조회한다.(트랜잭션-읽기모드)
     *
     * @param id 게시글Id
     * @return 게시글정보(Board Class)
     */
    @Transactional(readOnly = true)
    public Board 글상세보기(Integer id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> {
                    return new IllegalArgumentException("게시글조회오류");
                });
    }

    /**
     * 게시글 상세보기 화면에서 작성한 사용자와 수정하고자 하는 사용자가 같을경우 글 수정하기가 가능하다.(트랜잭션-수정모드)
     * 1. 게시글(게시글Id)정보를 이용하여 게시글 정보를 조회한다.(게시글정보 영속성컨텍스트에 영속화 진행)
     * 2. 입력파리미터로 받은 게시글정보를 이용하여 영속성컨텍스트에 있는 게시글정보를 변경한다.
     * 3. Jpa Persistence Context 변경감지를 통해서 게시글 정보를 수정한다.
     *
     * @param id           수정하고자 하는 게시글Id
     * @param requestBoard 수정하고자 하는 게시글 정보(Board Class)
     */
    @Transactional(readOnly = false)
    public void 글수정하기(Integer id, Board requestBoard) {
        //수정하고자 하는 게시글을 조회한다.
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> {
                    return new IllegalArgumentException("게시글조회 오류");
                });
        board.setTitle(requestBoard.getTitle());
        board.setContent(requestBoard.getContent());
    }

    /**
     * 게시글 상세보기 화면에서 게시글 작성한 사용자가 삭제를 진행하는 경우.(트랜잭션-수정모드)
     * 1. Board -< Reply 관계가 cascade = CascadeType.REMOVE
     * 2. 게시글 삭제시 관련 댓글들고 일제히 삭제가 된다.
     *
     * @param id 삭제하고자 하는 게시글Id
     */
    @Transactional(readOnly = false)
    public void 글삭제(Integer id) {
        boardRepository.deleteById(id);
    }

    @Transactional(readOnly = false)
    public void 댓글등록하기(ReplySaveRequestDto replySaveRequestDto) {

        //사용자정보조회하기
        User user = userRepository.findById(replySaveRequestDto.getUserId())
                .orElseThrow(() -> {
                    return new IllegalArgumentException("회원정보 조회오류");
                });

        //게시글조회하기
        Board board = boardRepository.findById(replySaveRequestDto.getBoardId())
                .orElseThrow(() -> {
                    return new IllegalArgumentException("게시글조회 오류");
                });

        //댓글생성하기
        Reply reply = Reply.builder()
                .content(replySaveRequestDto.getContent())
                .board(board)
                .user(user)
                .build();

        //댓글등록하기
        replyRepository.save(reply);
    }

    @Transactional(readOnly = false)
    public void 댓글등록하기_네이티브(ReplySaveRequestDto replySaveRequestDto) {
        //댓글등록하기
        int intCns = replyRepository.mSave(replySaveRequestDto.getUserId(),
                replySaveRequestDto.getBoardId(),
                replySaveRequestDto.getContent());
        System.out.println("intCns = " + intCns);
    }

    @Transactional(readOnly = false)
    public void 댓글삭제하기(Integer replyId) {
        //댓글삭제
        replyRepository.deleteById(replyId);
    }
}