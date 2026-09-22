package com.shivam.airBnb.dto;


import com.shivam.airBnb.entity.HotelContactInfo;
import lombok.Data;

@Data
public class HotelPriceResponseDTO {
    private Long id;
    private String name;
    private String city;

    private String[] photos ;

    private String[] amenities ;

    private HotelContactInfo hotelContactInfo ;

    private Double price;
}
