package ch.avelon.demo.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import lombok.Data;

@Entity
@Data
public class Measurement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "sensorId", insertable = false, updatable = false)
    @JsonIgnore
    private Sensor sensor;
    private Long sensorId;
    private int recordTime;
    private Float recordedValue;
}
