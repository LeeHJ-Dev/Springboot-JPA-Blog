package com.cos.blog.model;

import com.cos.blog.test.Member;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.core.annotation.Order;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Board")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 100)
    private String title;

    @Lob
    private String content; //샘머노트 라이브러리 <html> 태그가 섞여서 디자인됨

    @ColumnDefault("0")
    private int count;  //조회수

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    private User user; //객체지향은 오브젝트로 관리. 포린키가 아님.

    @OneToMany(mappedBy = "board", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    //mappedBy 연관관계의 주인이 아니다.(난 fk가 아니에요) db에 컬럼을 만들지 마세요.
    @JsonIgnoreProperties({"board"})
    @OrderBy("id desc")
    private List<Reply> replys = new ArrayList<>();

    @CreationTimestamp //insert, update
    private Timestamp createDate;
}
