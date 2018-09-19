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

    // Endpoints for Sensor

    @PostMapping(value = "/sensors")
    public void registerSensor(@RequestBody final Sensor sensor) {
        sensorRepository.save(sensor);
    }

    @PostMapping(value = "/measurements")
    public long addMeasurement(@RequestBody final Measurement measurement) {
        final Long id = measurementRepository.save(measurement).getId();
        liveValueService.addMeasurement(measurement);
        return id;
    }

    // Endpoints for UI

    @GetMapping(value = "/sensors")
    public Iterable<Sensor> getAllSensors() {
        return sensorRepository.findAll();
    }

    @GetMapping(value = "/liveValues/")
    public Collection<Measurement> getAllLatestMeasurements() {
        return liveValueService.getAll();
    }

    @GetMapping(value = "/liveValues/{sensorId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Measurement getLatestMeasurement(@PathVariable long sensorId) {
        return liveValueService.getLiveValue(sensorId);
    }

    @GetMapping(value = "/sensors/{sensorId}/measurements/{fromTs}/{toTs}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MeasurementPoint> interval(@PathVariable long sensorId, @PathVariable int fromTs, @PathVariable int toTs) {
        return measurementRepository.findBySensorInInterval(sensorId, fromTs, toTs);
    }
}
