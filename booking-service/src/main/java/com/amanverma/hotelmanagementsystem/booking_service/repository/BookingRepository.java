package com.amanverma.hotelmanagementsystem.booking_service.repository;

import com.amanverma.hotelmanagementsystem.booking_service.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

}
