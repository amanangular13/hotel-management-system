package com.amanverma.hotelmanagementsystem.booking_service.service.impl;

import com.amanverma.hotelmanagementsystem.booking_service.dto.BookingRequestDTO;
import com.amanverma.hotelmanagementsystem.booking_service.dto.BookingResponseDTO;
import com.amanverma.hotelmanagementsystem.booking_service.dto.PaymentResponseDTO;
import com.amanverma.hotelmanagementsystem.booking_service.dto.UserResponseDTO;
import com.amanverma.hotelmanagementsystem.booking_service.exception.ApiException;
import com.amanverma.hotelmanagementsystem.booking_service.feign.*;
import com.amanverma.hotelmanagementsystem.booking_service.model.Booking;
import com.amanverma.hotelmanagementsystem.booking_service.model.enums.BookingStatus;
import com.amanverma.hotelmanagementsystem.booking_service.repository.BookingRepository;
import com.amanverma.hotelmanagementsystem.booking_service.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;


@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final HotelClient hotelClient;
    private final InventoryClient inventoryClient;
    private final PaymentClient paymentClient;
    private final LoyaltyClient loyaltyClient;
    private final UserClient userClient;
    private final ModelMapper mapper;
    private UserResponseDTO user;

    @Override
    public BookingResponseDTO createBooking(BookingRequestDTO request) {

        user = userClient.getUser(request.getUserId());

        Boolean available = inventoryClient.isRoomAvailable(
                request.getRoomId(),
                request.getCheckInDate().toString(),
                request.getCheckOutDate().toString()
        );

        if (!available)
            throw new ApiException("Room not available for selected dates", HttpStatus.BAD_REQUEST);

        Double basePrice = hotelClient.getRoomPrice(request.getRoomId());
        Double totalAmount = basePrice * (request.getCheckOutDate().toEpochDay() - request.getCheckInDate().toEpochDay());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = LocalDateTime.now().format(formatter);
        int randomSuffix = new Random().nextInt(9000) + 1000;

        String bookingId = "BK" + timestamp + randomSuffix;

        PaymentResponseDTO payment = paymentClient.processPayment(request.getUserId(), totalAmount);
        if (!payment.getStatus().equals("SUCCESS")) {
            Booking booking = Booking.builder()
                    .bookingId(bookingId)
                    .userId(request.getUserId())
                    .roomId(request.getRoomId())
                    .checkInDate(request.getCheckInDate())
                    .checkOutDate(request.getCheckOutDate())
                    .paymentMode(request.getPaymentMode())
                    .totalAmount(totalAmount)
                    .loyaltyDiscount(0)
                    .useLoyaltyPoints(request.getUseLoyaltyPoints())
                    .status(BookingStatus.FAILED)
                    .build();

            throw new ApiException("Payment failed", HttpStatus.BAD_REQUEST);
        }

        Booking booking = Booking.builder()
                .bookingId(bookingId)
                .userId(request.getUserId())
                .roomId(request.getRoomId())
                .checkInDate(request.getCheckInDate())
                .checkOutDate(request.getCheckOutDate())
                .paymentMode(request.getPaymentMode())
                .totalAmount(totalAmount)
                .loyaltyDiscount(payment.getLoyaltyPointsUsed())
                .useLoyaltyPoints(request.getUseLoyaltyPoints())
                .status(BookingStatus.CONFIRMED)
                .build();

        Booking saved = bookingRepository.save(booking);
        loyaltyClient.awardPoints(request.getUserId(), totalAmount);

        return mapper.map(booking, BookingResponseDTO.class);
    }

    @Override
    public void updateBooking(Long bookingId) {
        Booking booking = bookingRepository.findByBookingId(bookingId)
                .orElseThrow(()-> new ApiException("Booking not found", HttpStatus.NOT_FOUND));

        PaymentResponseDTO payment = paymentClient.processPayment(user.getId(), booking.getTotalAmount());
        if (!payment.getStatus().equals("SUCCESS")) {
            throw new ApiException("Payment failed", HttpStatus.BAD_REQUEST);
        }

        booking.setLoyaltyDiscount(payment.getLoyaltyPointsUsed());
        booking.setStatus(BookingStatus.CONFIRMED);
        bookingRepository.save(booking);
    }

    @Override
    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findByBookingId(bookingId)
                .orElseThrow(()-> new ApiException("Booking not found", HttpStatus.NOT_FOUND));

        paymentClient.refund(booking.getTransactionId());
        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }
}
