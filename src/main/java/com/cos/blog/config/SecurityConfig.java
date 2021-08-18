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
        auth.userDetailsService(principalDetailService).passwordEncoder(encodePWD());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        System.out.println("SecurityConfig.configure 222");
        /**
         * 1. 사용자가 웹페이지에서 메뉴를 클릭하여 데이터를 요청한다.
         * 2-1) 사용자가 요청한 서비스가 인증이 필요한 페이지인지 확인 후 로그인이 필요하면 사용자 인증절차를 진행한다.
         * 2-2) 사용자가 요청한 서비스가 인증이 필요하지 않는 페이지이면 요청한 페이지를 제공한다.
         * 3. SpringSecurity SpringSecurityContext에서 Authentication 객체를 찾는다.
         * 4. Authentication 객체가 존재하지 않는다면 사용자에게 Login 페이지를 제공한다. (Default "/login")
         * 5. 사용자가 로그인 정보(Id/Pass)를 입력하고 로그인을 하면 UserDetailsService(로그인) -> UserDetails(사용자정보) Class 사용자 정보 등록한다.
         * 6. 로그인에 성공하면 Authentication 객체를 SpringSecurityContext에 담는다.
         */
        http
                // csrf 토큰 비활성화 ( 테스트시 걸어두는게 좋음)
                .csrf()
                .disable()

                //
                .authorizeRequests()

                /**
                 * andMatchers(Path..).permitAll()
                 * -> andMatchers 에서 설정한 리소스의 접근을 인증절차 없이 허용한다는 의미이다.
                 */
                .antMatchers("/", "/auth/**", "/js/**", "/css/**", "/image/**")
                .permitAll()

                /**
                 * anyRequest().authenticated()
                 * -> 모든 리소스를 의미하며 접근허용 리소스 및 인증 후 특정 레벨(user/admin)의 권한을 가진 사용자만 접근가능한 리소스를 설정한다.
                 * 그외 나머지 리소스들은 무조건 사용자 인증을 완료해야 접근이 가능하다는 의미이다.
                 */
                .anyRequest()
                .authenticated()

                //
                .and()
                .formLogin()

                /**
                 * loginPage("로그인 경로")
                 * -> 사용자가 따로 만든 로그인 페이지를 사용하려고 할 때 설정한다.
                 * -> 따로 설정하지 않으면 디폴트 URL "/login"이기 때문에 "/login"로 호출하면 스프링이 제공하는 기본 로그인 페이지가 호출된다.
                 * -> Path. Http://localhost:8000/auth/loginForm.jsp file call
                 */
                .loginPage("/auth/loginForm")

                /**
                 * loginPage("/auth/loginForm") 화면에서 사용자가 Id/Pass 입력 후 로그인을 진행하는 경우 UserDetailsService 인터페이스를 상속받은
                 * PrincipalDetailService Class 에서 UserDetails loadUserByUsername(String username) 함수를 재정의하여 사용자 정보를 조회한다.
                 * 사용자의 정보가 존재하면 UserDetails 인터페이스를 상속받은 PrincipalDetail Class 사용자 정보를 등록한다.(Security Session)
                 * 위 절차가 마무리 되면 "/auth/loginProc" Controller 호출하여 사용자 정보를 등록한다. 즉, 스프링시큐리티가 해당 주소로 요청오는 로그인 정보를
                 * 필터(가로채서)하여 사용자의 로그인 시도 및 사용자정보를 등록한다.
                 */
                .loginProcessingUrl("/auth/loginProc")      //스프링 시큐리티가 해당 주소로 요청오는 로그인을 가로채서 대신 로그인 해준다.

                // 로그인 성공하면 "/" => (Http://localhost:8000) 경로로 이동한다.
                .defaultSuccessUrl("/")
        ;

    }
}
