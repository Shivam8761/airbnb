package com.shivam.airBnb.controller;

import com.shivam.airBnb.service.RoomService;
import com.shivam.airBnb.dto.RoomDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/admin/hotels/{hotelId}/rooms")
@RestController
@RequiredArgsConstructor
@Slf4j
public class RoomAdminController {
    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<RoomDTO> CreateRoom(@PathVariable Long hotelId ,@RequestBody  RoomDTO roomDTO){
        log.info("Attempting to create room");
        RoomDTO room = roomService.createRoom(hotelId,roomDTO);
        log.info("Room created");
        return new ResponseEntity<>(room, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RoomDTO>> GetAllRooms(@PathVariable Long hotelId){
        log.info("Attempting to get all rooms");
        List<RoomDTO> rooms = roomService.getAllRoomsByHotelId(hotelId);
        return new ResponseEntity<>(rooms, HttpStatus.OK);
    }

    @GetMapping("/Rooms/{Roomid}")
    public ResponseEntity<RoomDTO> getRoomById(@PathVariable Long Roomid){
        RoomDTO room = roomService.getRoomById(Roomid);
        return ResponseEntity.ok(room);
    }

    @PutMapping("/{Roomid}")
    public ResponseEntity<RoomDTO> updateRoomById(@PathVariable Long hotelId,@PathVariable Long Roomid, @RequestBody RoomDTO roomDTO){
        RoomDTO room = roomService.updateRoomById(hotelId,Roomid, roomDTO);
        return ResponseEntity.ok(room);
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> DeleteRoomById(@PathVariable Long hotelId ,@PathVariable Long roomId){
        roomService.DeleteRoomById(hotelId,roomId);
        return ResponseEntity.noContent().build();
    }
}
