package com.cos.blog.service;

import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service    //스프링이 컴포넌트 스캔을 통해서 Bean에 등록해준다.(IOC)
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder encoder;

    /**
     * 사용자정보(회원)가 없을경우 회원가입이 진행되며, 사용자 정보 중 암호는 HashKey 사용하여 암호화 한다.
     *
     * @param user 사용자정보(회원)의 객체
     */
    @Transactional(readOnly = false)
    public void 회원가입(User user) {
        String rawPassword = user.getPassword();    //1234
        String encPassword = encoder.encode(rawPassword);
        //System.out.println("rawPassword = " + rawPassword);     //암호화가 안된것.
        //System.out.println("encPassword = " + encPassword);     //암호화가 된것.(해쉬)
        user.setRole(RoleType.USER);
        user.setPassword(encPassword);
        userRepository.save(user);
    }

    /**
     * 사용자 회원Id(pk) 정보의 이용하여 회원정보를 조회한다.(트랜잭션 읽기전용)
     *
     * @param id 회원Id(pk)
     * @return 사용자의 정보(회원)가 존재하는 경우 회원(User Class) 정보를 리턴하며, 사용자 정보가 없을경우 new IllegalArgumentException("") 처리를 한다.
     */
    @Transactional(readOnly = false)
    public User 회원조회(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> {
            return new IllegalArgumentException("회원정보 미존재");
        });
    }

    /**
     * 회원정보 수정 화면을 통해서 사용자정보(회원Id, 이메일(Email))를 입력받아 사용자 정보를 수정한다.(트랜잭션 수정)
     * 1. 사용자정보(회원Id)를 이용하여 사용자 정보를 조회한다.(사용자정보 영속성 컨텍스트 영속화 진행)
     * 2. OAuth(카카오) 인증 사용자가 아닌경우 이메일정보만 수정하며, 그 이외의 사용자에게는 패스워드, 이메일 정보를 수정한다.
     * 사용자 정보가 영속화 되어 있어 "회원수정" 서비스가 종료되면 영속성을 flush()하여 사용자 정보를 수정한다.
     *
     * @param user 변경하고자 하는 사용자의 정보를 입력받는다.
     */
    @Transactional(readOnly = false)
    public void 회원수정(User user) {
        /**
         * 사용자(회원Id)의 Id로 Jpa Persistence Context(에) 사용자 정보를 영속화 시킨다.
         */
        User persistence = userRepository.findById(user.getId())
                .orElseThrow(() -> {
                    return new IllegalArgumentException("회원미존재");
                });
        /**
         * 사용자정보(회원Id) 중 OAuth(네이버 등)로 등록된 사용자면 패스워드의 수정은 Skip 한다.
         */
        if (persistence.getOauth() == null || persistence.getOauth().equals("")) {
            persistence.setPassword(encoder.encode(user.getPassword()));
        }

        /**
         * Jpa Persistence Context(의) 사용자(회원) 객체를 변경하여 변경감지가 진행되면 수정이 되도록 한다.
         */
        persistence.setEmail(user.getEmail());
    }

    /**
     * 사용자정보(회원) 중 username(를) 이용하여 사용자정보를 조회한다. 사용자 정보가 있을경우 User Class(를) 리턴하며,
     * 사용자정보가 존재하지 않는경우 .orElseGet() -> Null 정보 리턴한다.
     *
     * @param username 사용자(회원) 이름
     * @return 사용자 User Class or Null
     */
    @Transactional(readOnly = true)
    public User 회원찾기(String username) {
        return userRepository.findByUsername(username)
                .orElseGet(() -> {
                    return null;
                });
    }
}
