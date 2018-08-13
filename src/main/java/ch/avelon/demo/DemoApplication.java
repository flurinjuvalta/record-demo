package ch.avelon.demo;

import ch.avelon.demo.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
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
        return sensorRepository.save(sensor).getId();
    }

    @GetMapping(value = "/sensors")
    public Iterable<Sensor> getAllSensors() {
        return sensorRepository.findAll();
    }

    @PostMapping(value = "/measurements")
    public long addMeasurement(@RequestBody final Measurement measurement) {
        final Long id = measurementRepository.save(measurement).getId();
        liveValueService.addMeasurement(measurement);
        return id;
    }

    @GetMapping(value = "/latest/measurements/")
    public Collection<Measurement> getAllLatestMeasurements() {
        return liveValueService.getAll();
    }

    @GetMapping(value = "/latest/measurements/{sensorId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Measurement getLatestMeasurement(@PathVariable long sensorId) {
        return liveValueService.getLatestMeasurement(sensorId);
    }

    @GetMapping(value = "/sensors/{sensorId}/measurements/{fromTs}/{toTs}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MeasurementPoint> test(@PathVariable long sensorId, @PathVariable int fromTs, @PathVariable int toTs) {
        return measurementRepository.findBySensorInInterval(sensorId, fromTs, toTs);
    }

    @PostMapping(value = "/calculate", produces = MediaType.APPLICATION_JSON_VALUE)
    public double calculate(@RequestBody String formula) throws ScriptException {
        final Pattern p = Pattern.compile("\\$(\\d+)");
        final Matcher m = p.matcher(formula);
        final SimpleBindings values = new SimpleBindings();

        while (m.find())
        {
            values.put(m.group(0), liveValueService.getLatestMeasurement(Long.valueOf(m.group(1))).getRecordedValue());
        }

        return (double) new ScriptEngineManager().getEngineByName("JavaScript").eval(formula, values);
    }
}
