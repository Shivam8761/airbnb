package com.shivam.airBnb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class HotelInfoDto {
    private HotelDTO hotel;
    private List<RoomPriceResponseDto> rooms;
}
