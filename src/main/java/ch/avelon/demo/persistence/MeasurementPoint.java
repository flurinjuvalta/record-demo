package ch.avelon.demo.persistence;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Flurin Juvalta <flurin.juvalta@avelon.ch>
 */
public interface MeasurementPoint {
    @JsonProperty("x")
    long getRecordTime();
    @JsonProperty("y")
    float getRecordedValue();
}
