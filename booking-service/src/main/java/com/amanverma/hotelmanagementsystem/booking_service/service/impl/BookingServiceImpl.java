package com.amanverma.hotelmanagementsystem.booking_service.service.impl;

import com.amanverma.hotelmanagementsystem.booking_service.dto.*;
import com.amanverma.hotelmanagementsystem.booking_service.exception.ApiException;
import com.amanverma.hotelmanagementsystem.booking_service.feign.*;
import com.amanverma.hotelmanagementsystem.booking_service.model.Booking;
import com.amanverma.hotelmanagementsystem.booking_service.model.enums.BookingStatus;
import com.amanverma.hotelmanagementsystem.booking_service.repository.BookingRepository;
import com.amanverma.hotelmanagementsystem.booking_service.service.BookingService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
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
    private final ModelMapper mapper;

    @Override
    @Retry(name = "bookingRetry")
    @CircuitBreaker(name = "bookingBreaker", fallbackMethod = "fallbackCreateBooking")
    public BookingResponseDTO createBooking(BookingRequestDTO request) {
        Boolean available = inventoryClient.isRoomAvailable(
                request.getRoomId(),
                request.getCheckInDate(),
                request.getCheckOutDate()
        ).getData();

        if (!available)
            throw new ApiException("Room not available for selected dates", HttpStatus.BAD_REQUEST);

        Double basePrice = hotelClient.getRoomPrice(request.getRoomId()).getData();
        Double totalAmount = basePrice * (request.getCheckOutDate().toEpochDay() - request.getCheckInDate().toEpochDay());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = LocalDateTime.now().format(formatter);
        int randomSuffix = new Random().nextInt(9000) + 1000;

        String bookingId = "BK" + timestamp + randomSuffix;

        PaymentRequestDTO paymentRequestDTO = PaymentRequestDTO.builder()
                .bookingId(bookingId)
                .userId(request.getUserId())
                .paymentMethod(request.getPaymentMode().toString())
                .amount(totalAmount)
                .useLoyaltyPoints(request.getUseLoyaltyPoints())
                .build();

        PaymentResponseDTO payment = paymentClient.processPayment(paymentRequestDTO).getData();

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
                    .transactionId(payment.getTransactionId())
                    .useLoyaltyPoints(request.getUseLoyaltyPoints())
                    .status(BookingStatus.FAILED)
                    .build();

            bookingRepository.save(booking);
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
                .transactionId(payment.getTransactionId())
                .loyaltyDiscount(payment.getLoyaltyPointsUsed())
                .useLoyaltyPoints(request.getUseLoyaltyPoints())
                .status(BookingStatus.CONFIRMED)
                .build();

        InventoryRequestDTO inventoryRequestDTO = InventoryRequestDTO.builder()
                .roomId(booking.getRoomId())
                .startDate(booking.getCheckInDate())
                .endDate(booking.getCheckOutDate())
                .build();

        inventoryClient.markBook(inventoryRequestDTO);
        Booking saved = bookingRepository.save(booking);
        return mapper.map(saved, BookingResponseDTO.class);
    }

    public BookingResponseDTO fallbackCreateBooking(BookingRequestDTO request, Throwable t) {
       throw new ApiException("Booking service is temporarily unavailable. Please try again later.", HttpStatus.SERVICE_UNAVAILABLE);
    }


    @Override
    @Retry(name = "bookingRetry")
    @CircuitBreaker(name = "bookingBreaker", fallbackMethod = "fallbackUpdateBooking")
    public BookingResponseDTO updateBooking(String bookingId) {
        Booking booking = bookingRepository.findByBookingId(bookingId)
                .orElseThrow(()-> new ApiException("Booking not found", HttpStatus.NOT_FOUND));

        PaymentRequestDTO paymentRequestDTO = PaymentRequestDTO.builder()
                .bookingId(booking.getBookingId())
                .userId(booking.getUserId())
                .paymentMethod(booking.getPaymentMode().toString())
                .amount(booking.getTotalAmount())
                .useLoyaltyPoints(booking.getUseLoyaltyPoints())
                .build();

        PaymentResponseDTO payment = paymentClient.processPayment(paymentRequestDTO).getData();
        if (!payment.getStatus().equals("SUCCESS")) {
            throw new ApiException("Payment failed", HttpStatus.BAD_REQUEST);
        }

        InventoryRequestDTO inventoryRequestDTO = InventoryRequestDTO.builder()
                .roomId(booking.getRoomId())
                .startDate(booking.getCheckInDate())
                .endDate(booking.getCheckOutDate())
                .build();

        inventoryClient.markBook(inventoryRequestDTO);

        booking.setLoyaltyDiscount(payment.getLoyaltyPointsUsed());
        booking.setStatus(BookingStatus.CONFIRMED);
        Booking saved = bookingRepository.save(booking);
        return mapper.map(saved, BookingResponseDTO.class);
    }

    public BookingResponseDTO fallbackUpdateBooking(String bookingId, Throwable t) {
        throw new ApiException("Booking service is temporarily unavailable. Please try again later.", HttpStatus.SERVICE_UNAVAILABLE);
    }

    @Override
    @Retry(name = "bookingRetry")
    @CircuitBreaker(name = "bookingBreaker", fallbackMethod = "fallbackCancelBooking")
    public BookingResponseDTO cancelBooking(String bookingId) {
        Booking booking = bookingRepository.findByBookingId(bookingId)
                .orElseThrow(()-> new ApiException("Booking not found", HttpStatus.NOT_FOUND));

        InventoryRequestDTO inventoryRequestDTO = InventoryRequestDTO.builder()
                .roomId(booking.getRoomId())
                .startDate(booking.getCheckInDate())
                .endDate(booking.getCheckOutDate())
                .build();

        inventoryClient.markAvailable(inventoryRequestDTO);
        paymentClient.refund(booking.getTransactionId());
        booking.setStatus(BookingStatus.CANCELLED);
        Booking saved = bookingRepository.save(booking);
        return mapper.map(saved, BookingResponseDTO.class);
    }

    public BookingResponseDTO cancelBooking(String bookingId, Throwable t) {
        throw new ApiException("Booking service is temporarily unavailable. Please try again later.", HttpStatus.SERVICE_UNAVAILABLE);
    }
}
