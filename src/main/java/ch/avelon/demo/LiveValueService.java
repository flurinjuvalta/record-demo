package ch.avelon.demo;

import ch.avelon.demo.persistence.Measurement;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class LiveValueService {

    private static final Map<Long, Measurement> liveValues = new HashMap<>();

    public void addMeasurement(final Measurement measurement) {
        liveValues.compute(measurement.getSensorId(), (k, v) ->
                v == null || v.getRecordTime() <= measurement.getRecordTime() ? measurement : v);
    }

    public Measurement getLiveValue(final long sensorId) {
        return liveValues.get(sensorId);
    }

    public Collection<Measurement> getAll() {
        return liveValues.values();
    }
}
