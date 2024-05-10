package ldpd.suso.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer quiz_id;

    @OneToOne
    @JoinColumn(name = "sign_id")
    private Sign sign;

    private Boolean correct;

    public Quiz(Sign sign) {
        this.sign = sign;
    }

    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }
}
