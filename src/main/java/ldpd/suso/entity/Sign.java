package ldpd.suso.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Sign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sign_id")
    private Integer id;

    private String title;

    private String sign_desc;

    private String filename;

    private String filepath;
}
