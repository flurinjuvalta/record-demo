package ch.avelon.demo.persistence;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface MeasurementPoint {
    @JsonProperty("x")
    int getRecordTime();
    @JsonProperty("y")
    float getRecordedValue();
}
