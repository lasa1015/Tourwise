package com.tourwise.backend.repository;

import com.tourwise.backend.model.ItinerarySavedItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItinerarySavedItemsRepository extends JpaRepository<ItinerarySavedItems, Long> {
}