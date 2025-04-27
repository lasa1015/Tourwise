package com.shaluo.tourwise.repository;

import com.shaluo.tourwise.model.ItinerarySaved;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItinerarySavedRepository extends JpaRepository<ItinerarySaved, Long> {

    // SELECT * FROM itinerary_saved WHERE user_id = ?;
    // 把查到的所有数据封装成一个一个 ItinerarySaved 对象，再打包成 List<ItinerarySaved> 返回
    List<ItinerarySaved> findByUserId(Long id);

}