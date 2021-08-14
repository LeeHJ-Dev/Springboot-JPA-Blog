package com.cos.blog.service;

import com.cos.blog.model.Board;
import com.cos.blog.model.User;
import com.cos.blog.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

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

    @Transactional(readOnly = true)
    public Board 글상세보기(Integer id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글조회오류"));
    }

    @Transactional(readOnly = false)
    public void 글삭제(Integer id) {
        boardRepository.deleteById(id);
    }

    @Transactional(readOnly = false)
    public void 글수정하기(Integer id, Board requestBoard) {
        System.out.println("id = " + id);

        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글조회 오류"));

        board.setTitle(requestBoard.getTitle());
        board.setContent(requestBoard.getContent());
    }
}