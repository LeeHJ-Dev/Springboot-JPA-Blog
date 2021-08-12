package com.cos.blog.test;

import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

@RestController
public class DummyControllerTest {

    @Autowired //의존성주입(DI)
    private UserRepository userRepository;

    @DeleteMapping(value = "/dummy/user/{id}")
    public String delete(@PathVariable("id") Integer id) {

        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            return "삭제에 실패하였습니다. 해당 id는 db에 없습니다.";
        }
        return "삭제되었습니다. id : " + id;
    }

    @Transactional(readOnly = false)
    @PutMapping(value = "/dummy/user/{id}")
    //json 데이터 요청 -> java object 로 변환해서 받아준다.
    //MessageConverter Jackson 라이브러리가 변환해서 준다.
    public User update(@PathVariable("id") Integer id, @RequestBody User requestUser) {
        User findUser = userRepository.findById(id).orElseThrow(() -> {
            return new IllegalArgumentException("유저없음 id: " + id);
        });

        findUser.setPassword(requestUser.getPassword());
        findUser.setEmail(requestUser.getEmail());
        return findUser;
    }

    @GetMapping(value = "/dummy/users")
    public List<User> list() {
        List<User> userList = userRepository.findAll();
        return userList;
    }

    //한페이지당 2건의 데이터를 리턴받아 화면에 제공한다.
    @GetMapping(value = "/dummy/user")
    public List<User> pageList(@PageableDefault(size = 2, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<User> userPage = userRepository.findAll(pageable);
        return userPage.getContent();
    }

    @GetMapping(value = "/dummy/user/{id}")
    public User detail(@PathVariable("id") int id) {
        User user = userRepository.findById(id).orElseThrow(new Supplier<IllegalArgumentException>() {
            @Override
            public IllegalArgumentException get() {
                return new IllegalArgumentException("해당유저는 없습니다. id : " + id);
            }
        });
        //요청: 웹브라우저
        //user 객체 -> 자바 오브젝트
        //변환 ( 웹브라우저가 이해할 수 있는 데이터 ) -> JSON
        //스프링부트 = MessageConverter라는 애가 응답시에 자동 작동
        //만약에 자바 오프젝트를 리턴하게 되면 MessageConverter => Jackson 라이브러리가 호출되어서
        //json으로 변환해서 리턴한다.
        return user;
    }


    @PostMapping(value = "/dummy/join")
    public String join(User user) {
        System.out.println("id = " + user.getId());
        System.out.println("username = " + user.getUsername());
        System.out.println("password = " + user.getPassword());
        System.out.println("email = " + user.getEmail());
        System.out.println("role = " + user.getRole());
        System.out.println("createDate = " + user.getCreateDate());

        user.setRole(RoleType.USER);
        userRepository.save(user);
        return "회원가입이 완료되었습니다.";
    }



}
