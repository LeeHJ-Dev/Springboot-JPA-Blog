package com.cos.blog.controller;

import com.cos.blog.model.KakaoProfile;
import com.cos.blog.model.OAuthToken;
import com.cos.blog.model.User;
import com.cos.blog.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

/**
 * 1. 인증이 안된 사용자들이 출입할 수 있는 경로를 /auth/** 허용한다.
 * 2. 그냥 주소가 / 이면 index.jsp 허용.
 * 3. static 이하에 있는 /js/**, /css/**, /image/** 허용.
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Value("${cos.key}")
    private String cosKey;

    /**
     * 고객이 회원가입을 버튼을 클릭했을경우 회원가입 폼을 제공한다.
     *
     * @return /WEB-INF/views/user/joinForm.jsp file return
     */
    @GetMapping(value = "/auth/joinForm")
    public String joinForm() {
        return "user/joinForm";
    }

    /**
     * 회원가입한 고객이 로그인 버튼을 클릭했을경우 로그인폼을 제공한다.
     *
     * @return /WEB-INF/views/user/loginForm.jsp file return
     */
    @GetMapping(value = "/auth/loginForm")
    public String loginForm() {
        return "user/loginForm";
    }

    /**
     * 회원정보 수정을 클릭했을경우 회원정보 수정 폼을 제공한다.
     *
     * @return /WEB-INF/views/user/updateForm.jsp file return
     */
    @GetMapping(value = "/user/updateForm")
    public String updateForm() {
        System.out.println("UserController.updateForm");
        return "/user/updateForm";
    }

    /**
     * @return
     * @ResponseBody Data를 리턴해주는 컨트롤러 함수
     */
    @GetMapping(value = "/auth/kakao/callback")
    public String kakaoCallback(String code) {

        //post 방식으로 key=value 데이터를 요청(카카오쪽으로)
        //Retrofit2, OkHttp, RestTemplate 라이브러리 사용
        RestTemplate rt = new RestTemplate();

        //HttpHeader 오프젝트 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpBody 오브젝트 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "b07e4bb2f98482c18c4ff837606b6499");
        params.add("redirect_uri", "http://localhost:8000/auth/kakao/callback");
        params.add("code", code);

        //Http Entity 생성
        // 1. HttpHeader
        // 2. HttpBody
        // -> 하나의 오브젝트에 담기 (HttpEntity)
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        // Http 요청하기 -> Post
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        //응답토큰
        /*
        "access_token":"ueKBiUQcgT7AQNM9OuH65iVg-EyvE5VEbinf5go9c04AAAF7R8I2EQ",
        "token_type":"bearer",
        "refresh_token":"LnbPA8LOaiVlXhR3A4QoZT0Hz9mDIMBEwmrJnAo9c04AAAF7R8I2EQ",
        "expires_in":21599,
        "scope":"account_email profile_image profile_nickname",
        "refresh_token_expires_in":5183999
        */

        //Gson, Json Simple, ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        OAuthToken oauthToken = null;

        try {
            oauthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        System.out.println("oauthToken.getAccess_token() = " + oauthToken.getAccess_token());

        //-----------------------------------------//
        // 사용자 정보 제공
        //-----------------------------------------//
        RestTemplate rt2 = new RestTemplate();

        //HttpHeader 오프젝트 생성
        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("Authorization", "Bearer " + oauthToken.getAccess_token());
        headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //Http Entity 생성
        // 1. HttpHeader
        // 2. HttpBody
        // -> 하나의 오브젝트에 담기 (HttpEntity)
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers2);

        // Http 요청하기 -> Post
        ResponseEntity<String> response2 = rt2.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class
        );


        //Gson, Json Simple, ObjectMapper
        ObjectMapper objectMapper2 = new ObjectMapper();
        KakaoProfile kakaoProfile = null;

        try {
            kakaoProfile = objectMapper2.readValue(response2.getBody(), KakaoProfile.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        System.out.println("카카오 아이디(번호) = " + kakaoProfile.getId());
        System.out.println("카카오 이메일 = " + kakaoProfile.getKakao_account().getEmail());


        System.out.println("블러그서버 유저내일 = " + kakaoProfile.getKakao_account().getEmail() + "_" + kakaoProfile.getId());
        System.out.println("블러그서버 이메일 = " + kakaoProfile.getKakao_account().getEmail());
        //UUID garbagePassword = UUID.randomUUID();
        System.out.println("블러그서버 패스워드 = " + cosKey);

        User kakaoUser = User.builder()
                .username(kakaoProfile.getKakao_account().getEmail() + "_" + kakaoProfile.getId())
                .email(kakaoProfile.getKakao_account().getEmail())
                .password(cosKey)
                .oauth("kakao")
                .build();


        //가입자 혹은 비가입자 체크 해서 처리
        User originUser = userService.회원찾기(kakaoUser.getUsername());
        if (originUser == null) {
            System.out.println("기존회원이 아닙니다.회원정보가 입력됩니다.");
            userService.회원가입(kakaoUser);
        }

        System.out.println("자동로그인을 시작합니다.");


        //로그인처리
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(kakaoUser.getUsername(), cosKey));
        SecurityContextHolder.getContext().setAuthentication(authentication);


        return "redirect:/";
    }



}
