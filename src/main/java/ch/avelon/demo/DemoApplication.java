package ch.avelon.demo;

import ch.avelon.demo.persistence.*;
import java.util.Collection;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DemoApplication {
    @NonNull
    private SensorRepository sensorRepository;
    @NonNull
    private MeasurementRepository measurementRepository;
    @NonNull
    private LiveValueService liveValueService;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @PostMapping(value = "/sensors")
    public long addSensor(@RequestBody final Sensor sensor) {
        sensor.setId(null);
        return sensorRepository.save(sensor).getId();
    }

    @PostMapping(value = "/measurements")
    public long addMeasurement(@RequestBody final Measurement measurement) {
        measurement.setId(null);
        final Long id = measurementRepository.save(measurement).getId();
        liveValueService.addMeasurement(measurement);
        return id;
    }

    @GetMapping(value = "/measurements/latest")
    public Collection<Measurement> getAllLAtestMeasurements() {
        return liveValueService.getAll();
    }

    @GetMapping(value = "/sensors/{sensorId}/measurements/latest", produces = MediaType.APPLICATION_JSON_VALUE)
    public Measurement getLatestMeasurement(@PathVariable long sensorId) {
        return liveValueService.getLatestMeasurement(sensorId);
    }

    @GetMapping(value = "/sensors/{sensorId}/measurements/{fromTs}/{toTs}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MeasurementPoint> test(@PathVariable long sensorId, @PathVariable int fromTs, @PathVariable int toTs) {
        return measurementRepository.findBySensorInInterval(sensorId, fromTs, toTs);
    }
}
