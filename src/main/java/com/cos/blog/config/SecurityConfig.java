package com.cos.blog.config;

import com.cos.blog.config.auth.PrincipalDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.management.MXBean;

//빈 등록: 스프링 컨테이너에서 객체를 관리할 수 있게 하는 것.
@Configuration      //빈등록(IoC 관리)
@EnableWebSecurity  //시큐리티 필터 추가 => 스프링 시큐리티가 활성화가 되어 있는데, 어떤 설정을 해당 파일에서 하겠다.
@EnableGlobalMethodSecurity(prePostEnabled = true)  //특정 주소로 접근을 하면 권한 및 인증을 미리 체크하겠다는 뜻.
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PrincipalDetailService principalDetailService;

    @Bean   //IoC가 되요!
    public BCryptPasswordEncoder encodePWD() {
        return new BCryptPasswordEncoder();
    }

    //시큐리티가 대신 로그인해주는데 password를 가로채기를 하는데, 해당 password가 뭘로 해쉬가 되어 회원가입이 되었는지
    //알아야 같은 해쉬로 암호화해서 db에 있는 해쉬랑 비교할 수 있음.
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        System.out.println("SecurityConfig.configure 111");

        auth.userDetailsService(principalDetailService)
                .passwordEncoder(encodePWD());
    }


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        System.out.println("SecurityConfig.configure 222");
        http
                // csrf 토큰 비활성화 ( 테스트시 걸어두는게 좋음)
                .csrf()
                .disable()

                //
                .authorizeRequests()

                //antMatchers()에 등록된 Path 경로는 인증없으 접근을 허용한다.
                .antMatchers("/", "/auth/**", "/js/**", "/css/**", "/image/**")
                .permitAll()

                //antMatchers()에 등록된 Path 경로이외의 요청에 대해서는 사용자 인증이 되어 있어야 접근이 가능하다.
                .anyRequest()
                .authenticated()

                //
                .and()
                .formLogin()

                // "/auth/loginForm" 로그인 페이지에서 로그인을 진행하는경우
                .loginPage("/auth/loginForm")

                // "/auth/loginProc" Path 경로로 로그인(id/pass) 버튼을 클릭했을경우
                // public class PrincipalDetailService implements UserDetailsService {
                // UserDetailsService 인터페이스를 상속받은 PrincipalDetailService 클래스의 loadUserByUsername 함수를 호출한다.
                .loginProcessingUrl("/auth/loginProc")      //스프링 시큐리티가 해당 주소로 요청오는 로그인을 가로채서 대신 로그인 해준다.

                // 로그인 성공하면 "/" => (Http://localhost:8000) 경로로 이동한다.
                .defaultSuccessUrl("/")
        ;

    }
}
