package com.shaluo.tourwise.repository;

import com.shaluo.tourwise.model.BusynessPrediction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BusynessPredictionRepository extends JpaRepository<BusynessPrediction, Long> {

    Optional<BusynessPrediction> findByTaxiZoneAndDatetime(Integer taxiZone, LocalDateTime datetime);

    List<BusynessPrediction> findByDatetimeBetween(LocalDateTime start, LocalDateTime end);

    List<BusynessPrediction> findByTaxiZoneAndDatetimeBetween(Integer taxiZone, LocalDateTime start, LocalDateTime end);
}
