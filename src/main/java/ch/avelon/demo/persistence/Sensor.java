package ch.avelon.demo.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

@Entity
@Data
public class Sensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String location;
    @JsonIgnore
    @OneToMany(mappedBy = "sensor")
    private List<Measurement> measurements;
}
