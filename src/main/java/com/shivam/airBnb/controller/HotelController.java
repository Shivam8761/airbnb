package com.shivam.airBnb.controller;

import com.shivam.airBnb.service.BookingService;
import com.shivam.airBnb.service.HotelService;
import com.shivam.airBnb.dto.BookingDTO;
import com.shivam.airBnb.dto.HotelDTO;
import com.shivam.airBnb.dto.HotelReportDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequestMapping("/admin/hotels")
@RestController
@RequiredArgsConstructor
@Slf4j
public class HotelController {

        private final HotelService hotelService;
        private final BookingService bookingService;

        @PostMapping
        @Operation(summary = "Create a new hotel", tags = {"Admin Hotel"})
        public ResponseEntity<HotelDTO> createHotel(@RequestBody  HotelDTO hotelDTO) {
            log.info("Attempting to create hotel with name" + hotelDTO.getName());
            HotelDTO hotel = hotelService.createHotel(hotelDTO);
            return  new ResponseEntity<>(hotel, HttpStatus.CREATED);
        }

        @GetMapping
        @Operation(summary = "Get All its hotel ", tags = {"Admin Hotel"})
        public ResponseEntity<List<HotelDTO>> getAllHotels() {
            log.info("Attempting to get all hotels");
            List<HotelDTO> hotels =  hotelService.getAllHotels() ;
            return new ResponseEntity<>(hotels,HttpStatus.FOUND) ;
        }

        @GetMapping("/{HotelId}")
        @Operation(summary = "Get a hotel by Id", tags = {"Admin Hotel"})
        public ResponseEntity<HotelDTO> getHotelById(@PathVariable Long HotelId){
            HotelDTO hotelDTO = hotelService.getHotelById(HotelId);
            return  ResponseEntity.ok(hotelDTO);
        }

        @PutMapping("/{HotelId}")
        @Operation(summary = "Updating a hotel by Id", tags = {"Admin Hotel"})
        public ResponseEntity<HotelDTO>  updateHotelById(@PathVariable Long HotelId,@RequestBody HotelDTO hotelDTO) {
            HotelDTO hotelDTO1 = hotelService.updateHotelById(HotelId , hotelDTO);
            return new ResponseEntity<>(hotelDTO1, HttpStatus.OK);
        }

        @DeleteMapping("/{HotelId}")
        public ResponseEntity<Void> deleteById(@PathVariable Long HotelId){
            hotelService.DeleteHotelById(HotelId);
            return ResponseEntity.noContent().build() ;
        }

    @PatchMapping("/active/{hotelId}")
    public ResponseEntity<Void> activateHotel(@PathVariable Long hotelId) {
        hotelService.activateHotel(hotelId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/deactive/{hotelId}")
    public ResponseEntity<Void> deactivateHotel(@PathVariable Long hotelId) {
        hotelService.deactivateHotel(hotelId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{hotelId}/bookings")
    @Operation(summary = "Get all bookings of a hotel", tags = {"Admin Bookings"})
    public ResponseEntity<List<BookingDTO>> getAllBookingsByHotelId(@PathVariable Long hotelId) {
        return ResponseEntity.ok(bookingService.getAllBookingsByHotelId(hotelId));
    }

    @GetMapping("/{hotelId}/reports")
    @Operation(summary = "Generate a bookings report of a hotel", tags = {"Admin Bookings"})
    public ResponseEntity<HotelReportDto> getHotelReport(@PathVariable Long hotelId,
                                                         @RequestParam(required = false) LocalDate startDate,
                                                         @RequestParam(required = false) LocalDate endDate) {

        if (startDate == null) startDate = LocalDate.now().minusMonths(1);
        if (endDate == null) endDate = LocalDate.now();

        return ResponseEntity.ok(bookingService.getHotelReport(hotelId, startDate, endDate));
    }

}
