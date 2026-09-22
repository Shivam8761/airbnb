package com.shivam.airBnb.controller;


import com.shivam.airBnb.entity.Guest;
import com.shivam.airBnb.service.BookingService;
import com.shivam.airBnb.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/booking")
@RequiredArgsConstructor
public class HotelBookingController {
    private final  BookingService bookingService;

    @PostMapping("/init")
    public ResponseEntity<BookingDTO> initializeBooking(@RequestBody BookingRequest bookingRequest){
        bookingService.initializeBooking(bookingRequest) ;
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{BookingId}/addGuests")
    public ResponseEntity<BookingDTO> addGuest(@PathVariable Long BookingId ,@RequestBody List<Long> guestList){
        BookingDTO booking = bookingService.addGuest(BookingId,guestList);
        return new ResponseEntity<>(booking, HttpStatus.OK);
    }

    @PostMapping("/{bookingId}/payments")
    public ResponseEntity<BookingPaymentInitResponseDto> initiatePayment(@PathVariable Long bookingId) {
        String sessionUrl = bookingService.initiatePayments(bookingId);
        return ResponseEntity.ok(new BookingPaymentInitResponseDto(sessionUrl));
    }

    @PostMapping("/{bookingId}/cancel")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long bookingId) {
        bookingService.cancelBooking(bookingId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{bookingId}/status")
    public ResponseEntity<BookingStatusResponseDto> getBookingStatus(@PathVariable Long bookingId) {
        return ResponseEntity.ok(new BookingStatusResponseDto(bookingService.getBookingStatus(bookingId)));
    }
}
