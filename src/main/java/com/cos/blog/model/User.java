package com.cos.blog.model;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity //user class -> mysql 테이블생성
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Data
@Builder
//@DynamicInsert //insert시에 null인 필드를 제외시켜준다.
public class User {

    @Id //primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; //시퀀스, auto_increment

    @Column(nullable = false, length = 30)
    private String username; //아이디

    @Column(nullable = false, length = 100)
    private String password; //패스워드

    @Column(nullable = false, length = 50)
    private String email;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = true, length = 50)
    private RoleType role;                  //USER, ADMIN

    @CreationTimestamp //시간이 자동 입력
    private Timestamp createDate;

 //   @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
 //   private List<Board> boardList = new ArrayList<Board>();
}