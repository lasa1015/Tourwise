package com.tourwise.scrapper.repository;

import com.tourwise.scrapper.model.EventData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<EventData, UUID> {


    @Query("""
           SELECT COUNT(e) > 0
           FROM EventData e
           WHERE e.name = :name
              OR e.eventSiteUrl = :eventSiteUrl
              OR e.imageUrl     = :imageUrl
           """)
    boolean existsEventByNameOrUrl(@Param("name") String name,
                                   @Param("eventSiteUrl") String eventSiteUrl,
                                   @Param("imageUrl") String imageUrl);


    EventData findByNameOrEventSiteUrlOrImageUrl(String name,
                                                 String eventSiteUrl,
                                                 String imageUrl);
}
