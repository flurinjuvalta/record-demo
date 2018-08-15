package ch.avelon.demo.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

@Entity
@Data
public class Sensor {
    @Id
    private Long id;
    private String name;
    private String location;
    @JsonIgnore
    @OneToMany
    private List<Measurement> measurements;
}
