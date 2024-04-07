package ldpd.suso.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
public class Correct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cor_id;

    private boolean cor;    //맞혔는지 여부

    @ManyToOne
    private Quiz quiz;

    private Integer quiz_id;

    @ManyToOne  //사용자 한명이 답 여러개 가능
    private Member member;

    private Integer member_id;

    private String mem_ans;

    @CreationTimestamp
    private Timestamp createdAt;

    public Correct(Integer quiz_id, Integer member_id){
        this.quiz_id = quiz_id;
        this.member_id = member_id;
    }

}
