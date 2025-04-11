package com.tourwise.backend.repository;

import com.tourwise.backend.model.ItinerarySaved;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItinerarySavedRepository extends JpaRepository<ItinerarySaved, Long> {
    List<ItinerarySaved> findByUserId(Long id);
}