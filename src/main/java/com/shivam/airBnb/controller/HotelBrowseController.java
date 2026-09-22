package com.shivam.airBnb.controller;


import com.shivam.airBnb.service.HotelService;
import com.shivam.airBnb.service.InventoryService;
import com.shivam.airBnb.dto.HotelInfoDto;
import com.shivam.airBnb.dto.HotelInfoRequestDto;
import com.shivam.airBnb.dto.HotelPriceResponseDTO;
import com.shivam.airBnb.dto.HotelSearchRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
public class HotelBrowseController {
    private final InventoryService inventoryService;
    private final HotelService hotelService;

    @GetMapping("/search")
    public ResponseEntity<Page<HotelPriceResponseDTO>> searchHotel(@RequestBody HotelSearchRequest hotelSearchRequest){

        var Page = inventoryService.searchHotel(hotelSearchRequest) ;
        return ResponseEntity.ok(Page) ;
    }

    @GetMapping("/{hotelId}/info")
    @Operation(summary = "Get a hotel info by hotelId", tags = {"Browse Hotels"})
    public ResponseEntity<HotelInfoDto> getHotelInfo(@PathVariable Long hotelId, @RequestBody HotelInfoRequestDto hotelInfoRequestDto) {
        return ResponseEntity.ok(hotelService.getHotelInfoById(hotelId, hotelInfoRequestDto));
    }
}
