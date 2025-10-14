package com.amanverma.hotelmanagementsystem.booking_service.service.impl;

import com.amanverma.hotelmanagementsystem.booking_service.dto.BookingRequestDTO;
import com.amanverma.hotelmanagementsystem.booking_service.dto.BookingResponseDTO;
import com.amanverma.hotelmanagementsystem.booking_service.dto.LoyaltyResponseDTO;
import com.amanverma.hotelmanagementsystem.booking_service.dto.PaymentResponseDTO;
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

    @Override
    public BookingResponseDTO createBooking(BookingRequestDTO request) {
        if (!userClient.userExists(request.getUserId()))
            throw new ApiException("User not found", HttpStatus.NOT_FOUND);

        Boolean available = inventoryClient.isRoomAvailable(
                request.getRoomId(),
                request.getCheckInDate().toString(),
                request.getCheckOutDate().toString()
        );

        if (!available)
            throw new ApiException("Room not available for selected dates", HttpStatus.BAD_REQUEST);

        Double basePrice = hotelClient.getRoomPrice(request.getRoomId());
        Double totalAmount = basePrice * (request.getCheckOutDate().toEpochDay() - request.getCheckInDate().toEpochDay());

        Double discount = 0.0;
        if (Boolean.TRUE.equals(request.getUseLoyaltyPoints())) {
            LoyaltyResponseDTO loyalty = loyaltyClient.redeemPoints(request.getUserId(), totalAmount);
            discount = loyalty.getDiscountAmount();
            totalAmount -= discount;
        }

        PaymentResponseDTO payment = paymentClient.processPayment(request.getUserId(), totalAmount);
        if (!payment.getSuccess())
            throw new ApiException("Payment failed", HttpStatus.BAD_REQUEST);

        Booking booking = Booking.builder()
                .userId(request.getUserId())
                .roomId(request.getRoomId())
                .checkInDate(request.getCheckInDate())
                .checkOutDate(request.getCheckOutDate())
                .paymentMode(request.getPaymentMode())
                .totalAmount(totalAmount)
                .loyaltyDiscount(discount)
                .usedLoyaltyPoints(request.getUseLoyaltyPoints())
                .status(BookingStatus.CONFIRMED)
                .build();

        bookingRepository.save(booking);
        loyaltyClient.awardPoints(request.getUserId(), totalAmount);

        return mapper.map(booking, BookingResponseDTO.class);
    }
}
