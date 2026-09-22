package com.shivam.airBnb.service;

import com.shivam.airBnb.entity.Room;
import com.shivam.airBnb.dto.HotelPriceResponseDTO;
import com.shivam.airBnb.dto.HotelSearchRequest;
import com.shivam.airBnb.dto.InventoryDto;
import com.shivam.airBnb.dto.UpdateInventoryRequestDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface InventoryService {

    public void initializeRoomForAYear(Room room ) ;

    public void deleteAllInventories(Room room) ;

    Page<HotelPriceResponseDTO> searchHotel(HotelSearchRequest hotelSearchRequest);

    List<InventoryDto> getAllInventoryByRoom(Long roomId);

    void updateInventory(Long roomId, UpdateInventoryRequestDto updateInventoryRequestDto);
}
