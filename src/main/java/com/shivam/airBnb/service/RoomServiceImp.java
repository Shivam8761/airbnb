package com.shivam.airBnb.service;

import com.shivam.airBnb.entity.Hotel;
import com.shivam.airBnb.entity.Room;
import com.shivam.airBnb.entity.User;
import com.shivam.airBnb.exceptions.ResourceNotFoundException;
import com.shivam.airBnb.exceptions.UnAuthorisedException;
import com.shivam.airBnb.repository.HotelRepository;
import com.shivam.airBnb.repository.RoomRepository;
import com.shivam.airBnb.dto.RoomDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.shivam.airBnb.utility.AppUtils.getCurrentUser;

@RequiredArgsConstructor
@Slf4j
@Service
public class RoomServiceImp implements RoomService{

    private final InventoryService inventoryService;
    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper ;
    private final RoomRepository roomRepository;

    @Override
    public RoomDTO createRoom(Long hotelId ,RoomDTO roomDTO) {
        log.info("Creating a new room in hotel with ID: {}", hotelId);
        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: "+hotelId));

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())) {
            throw new UnAuthorisedException("This user does not own this hotel with id: "+hotelId);
        }

        Room room = modelMapper.map(roomDTO, Room.class);
        room.setHotel(hotel);
        room = roomRepository.save(room);

        if (hotel.getActive()) {
            inventoryService.initializeRoomForAYear(room);
        }

        return modelMapper.map(room, RoomDTO.class);
    }

    @Override
    public List<RoomDTO> getAllRoomsByHotelId(Long hotelId){
        log.info("Getting all rooms in hotel with ID: {}", hotelId);
        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: "+hotelId));

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())) {
            throw new UnAuthorisedException("This user does not own this hotel with id: "+hotelId);
        }

        return hotel.getRooms()
                .stream()
                .map((element) -> modelMapper.map(element, RoomDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public RoomDTO getRoomById(Long id) {
        log.info("getting the room with this id : " + id) ;
        Room room = roomRepository
                .findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Room not found with this id : " + id)) ;
        return modelMapper.map(room,RoomDTO.class);
    }

    @Override
    public RoomDTO updateRoomById(Long hotelId,Long roomId, RoomDTO roomDTO) {
        log.info("Updating the room with ID: {}", roomId);
        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: "+hotelId));

        User user = getCurrentUser();
        if(!user.equals(hotel.getOwner())) {
            throw new UnAuthorisedException("This user does not own this hotel with id: "+hotelId);
        }

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: "+roomId));

        modelMapper.map(roomDTO, room);
        room.setId(roomId);

//        TODO: if price or inventory is updated, then update the inventory for this room
        room = roomRepository.save(room);

        return modelMapper.map(room, RoomDTO.class);
    }

    @Override
    @Transactional
    public void DeleteRoomById(Long hotelId, Long roomId) {
        log.info("Deleting the room with ID: {}", roomId);
        Room room = roomRepository
                .findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: "+roomId));

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(room.getHotel().getOwner())) {
            throw new UnAuthorisedException("This user does not own this room with id: "+roomId);
        }

        inventoryService.deleteAllInventories(room);
        roomRepository.deleteById(roomId);
    }
}
