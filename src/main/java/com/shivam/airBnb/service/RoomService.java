package com.shivam.airBnb.service;

import com.shivam.airBnb.dto.RoomDTO;

import java.util.List;

public interface RoomService {
    public RoomDTO createRoom(Long HotelId ,RoomDTO roomDTO);

    public List<RoomDTO> getAllRoomsByHotelId(Long HotelId);

    public RoomDTO getRoomById(Long id) ;

    public RoomDTO updateRoomById(Long hotelId,Long RoomId ,RoomDTO roomDTO) ;

    public void DeleteRoomById(Long hotelId ,Long roomId) ;

}
