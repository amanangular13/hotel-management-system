package com.amanverma.hotelmanagementsystem.hotel_service.repository;

import com.amanverma.hotelmanagementsystem.hotel_service.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

}

