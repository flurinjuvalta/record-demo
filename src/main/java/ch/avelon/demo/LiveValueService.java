package ch.avelon.demo;

import ch.avelon.demo.persistence.Measurement;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 * @author Flurin Juvalta <flurin.juvalta@avelon.ch>
 */
@Service
public class LiveValueService {

    public static final Map<Long, Measurement> latestMeasurements = new HashMap<>();

    public void addMeasurement(final Measurement measurement) {
        latestMeasurements.compute(measurement.getSensorId(), (k, v) ->
                v == null || v.getRecordTime() <= measurement.getRecordTime() ? measurement : v);
    }

    public Measurement getLatestMeasurement(final long sensorId) {
        return latestMeasurements.get(sensorId);
    }

    public Collection<Measurement> getAll() {
        return latestMeasurements.values();
    }
}
