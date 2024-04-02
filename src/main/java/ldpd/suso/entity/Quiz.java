package ldpd.suso.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer quiz_id;

    private String answer;  //퀴즈의 답

    private String description; //설명

    private String filename;

    private String filepath;
}
