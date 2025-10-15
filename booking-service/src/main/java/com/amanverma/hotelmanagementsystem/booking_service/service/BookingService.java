package com.amanverma.hotelmanagementsystem.booking_service.service;

import com.amanverma.hotelmanagementsystem.booking_service.dto.BookingRequestDTO;
import com.amanverma.hotelmanagementsystem.booking_service.dto.BookingResponseDTO;

public interface BookingService {
    BookingResponseDTO createBooking(BookingRequestDTO request);
    void updateBooking(Long bookingId);
    void cancelBooking(Long bookingId);
}
