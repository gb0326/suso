package ldpd.suso.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Data
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ans_id;

    @ManyToOne  //사용자 한명이 답 여러개 가능
    private Member member;

    private String ans;

    @CreationTimestamp
    private Timestamp createdAt;

}
