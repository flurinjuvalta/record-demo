package ch.avelon.demo.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface MeasurementRepository extends CrudRepository<Measurement, Long> {
    @Query("SELECT m from Measurement m " +
            "WHERE m.sensorId = :sensorId " +
            "AND m.recordTime BETWEEN :fromTs and :toTs " +
            "ORDER BY m.recordTime")
    List<MeasurementPoint> findBySensorInInterval(
            @Param("sensorId") long sensorId,
            @Param("fromTs") int fromTs,
            @Param("toTs") int toTs);
}
