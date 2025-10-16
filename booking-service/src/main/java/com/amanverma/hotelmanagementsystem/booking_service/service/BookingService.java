package com.amanverma.hotelmanagementsystem.booking_service.service;

import com.amanverma.hotelmanagementsystem.booking_service.dto.BookingRequestDTO;
import com.amanverma.hotelmanagementsystem.booking_service.dto.BookingResponseDTO;

public interface BookingService {
    BookingResponseDTO createBooking(BookingRequestDTO request);
    BookingResponseDTO updateBooking(String bookingId);
    BookingResponseDTO cancelBooking(String bookingId);
}
