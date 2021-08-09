package com.cos.blog.test;

import org.springframework.web.bind.annotation.*;

/**
 * @Controller : 사용자가 요청 -> 응답(Html 파일)
 * @RestController : 사용자가 요청 -> 응답(Data)
 */
@RestController
public class HttpControllerTest {

    private static final String TAG = "HttpControllerTest : ";

    @GetMapping(value = "/http/lombok")
    public String lombokTest(){
        Member member = Member.builder()
                .username("Lee")
                .password("1234")
                .email("xxapara@ibksystem.co.kr")
                .build();

        return "lombok";
    }

    //인터넷 브라우저 요청은 무조건 get요청밖에 할 수 없다.
    //http://localhost:8080/http/get(select)
    @GetMapping(value = "/http/get")
    //public String getTest(@RequestParam("id") int id , @RequestParam("username") String username){
    public String getTest(Member m){
        return "get 요청" + m.getId() + "," + m.getUsername()+ "," + m.getPassword()+ "," + m.getEmail();
    }

    //http://localhost:8080/http/get(insert)
    @PostMapping(value = "/http/post")
    public String postTest(Member m){
        return "post 요청" + m.getId() + "," + m.getUsername()+ "," + m.getPassword()+ "," + m.getEmail();
    }

    //http://localhost:8080/http/get(update)
    @PutMapping(value = "/http/put")
    public String putTest(){
        return "put 요청";
    }

    //http://localhost:8080/http/get(delete)
    @DeleteMapping(value = "/http/delete")
    public String deleteTest(){
        return "delete 요청";
    }
}
