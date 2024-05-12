package ldpd.suso.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


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

    public Quiz(Sign sign) {
        this.sign = sign;
    }

}
