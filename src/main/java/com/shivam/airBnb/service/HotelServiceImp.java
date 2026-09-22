package com.shivam.airBnb.service;

import com.shivam.airBnb.entity.Hotel;
import com.shivam.airBnb.entity.Room;
import com.shivam.airBnb.entity.User;
import com.shivam.airBnb.exceptions.ResourceNotFoundException;
import com.shivam.airBnb.exceptions.UnAuthorisedException;
import com.shivam.airBnb.repository.HotelRepository;
import com.shivam.airBnb.repository.InventoryRepository;
import com.shivam.airBnb.repository.RoomRepository;
import com.shivam.airBnb.dto.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import static com.shivam.airBnb.utility.AppUtils.getCurrentUser;

@Service
@RequiredArgsConstructor
@Slf4j
public class HotelServiceImp implements HotelService {

    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper ;
    private final InventoryService inventoryService;
    private final RoomRepository roomRepository;
    private final InventoryRepository inventoryRepository;


    @Override
    public HotelDTO createHotel(HotelDTO hotelDTO) {
        log.info("Creating a new hotel with name: {}", hotelDTO.getName());
        Hotel hotel = modelMapper.map(hotelDTO, Hotel.class);
        hotel.setActive(false);

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        hotel.setOwner(user);

        hotel = hotelRepository.save(hotel);
        log.info("Created a new hotel with ID: {}", hotelDTO.getId());
        return modelMapper.map(hotel, HotelDTO.class);
    }

    @Override
    public HotelDTO getHotelById(Long id) {
        log.info("Getting the hotel with ID: {}", id);
        Hotel hotel = hotelRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: "+id));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!user.equals(hotel.getOwner())) {
            throw new UnAuthorisedException("This user does not own this hotel with id: "+id);
        }

        return modelMapper.map(hotel, HotelDTO.class);
    }

    @Override
    public HotelDTO updateHotelById(Long id ,HotelDTO hotelDTO) {

        log.info("Updating the hotel with ID: {}", id);
        Hotel hotel = hotelRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: "+id));

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())) {
            throw new UnAuthorisedException("This user does not own this hotel with id: "+id);
        }

        modelMapper.map(hotelDTO, hotel);
        hotel.setId(id);
        hotel = hotelRepository.save(hotel);
        return modelMapper.map(hotel, HotelDTO.class);
    }

    @Override
    @Transactional
    public void activateHotel(Long HotelId) {
    log.info("Activating hotel with this ID: " + HotelId) ;

    Hotel hotel1 = hotelRepository
            .findById(HotelId)
            .orElseThrow(()->new ResourceNotFoundException ("Hotel not found with this id :" + HotelId) ) ;

    hotel1.setActive(true);

    for(Room room : hotel1.getRooms()){
        inventoryService.initializeRoomForAYear(room);
    }
//    hotel = hotelRepository.save(hotel1);
    log.info("Activated hotel with this ID:  " + HotelId) ;
    }

    @Override
    @Transactional
    public void deactivateHotel(Long HotelId) {
        log.info("DeActivating hotel with this ID: " + HotelId) ;

        Hotel hotel1 = hotelRepository
                .findById(HotelId)
                .orElseThrow(()->new ResourceNotFoundException ("Hotel not found with this id :" + HotelId) ) ;

        hotel1.setActive(false);

//    hotel = hotelRepository.save(hotel1);
        log.info("DeActivated hotel with this ID:  " + HotelId) ;
    }


    @Override
    @Transactional
    public void DeleteHotelById(Long HotelId) {
        Hotel hotel = hotelRepository
                .findById(HotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: "+HotelId));

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())) {
            throw new UnAuthorisedException("This user does not own this hotel with id: "+HotelId);
        }


        for(Room room: hotel.getRooms()) {
            inventoryService.deleteAllInventories(room);
            roomRepository.deleteById(room.getId());
        }
        hotelRepository.deleteById(HotelId);
    }

    public void ExistsById(Long id) {
        boolean exists = hotelRepository.existsById(id);
        if (!exists) {
            throw new ResourceNotFoundException("Hotel not found with this id :" + id);
        }
    }

    @Override
    public List<HotelDTO> getAllHotels() {
        User user = getCurrentUser();
        log.info("Getting all hotels for the admin user with ID: {}", user.getId());
        List<Hotel> hotels = hotelRepository.findByOwner(user);

        return hotels
                .stream()
                .map((element) -> modelMapper.map(element, HotelDTO.class))
                .collect(Collectors.toList());
    }

    //    public method
    @Override
    public HotelInfoDto getHotelInfoById(Long hotelId, HotelInfoRequestDto hotelInfoRequestDto) {
        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: "+hotelId));

        long daysCount = ChronoUnit.DAYS.between(hotelInfoRequestDto.getStartDate(), hotelInfoRequestDto.getEndDate())+1;

        List<RoomPriceDto> roomPriceDtoList = inventoryRepository.findRoomAveragePrice(hotelId,
                hotelInfoRequestDto.getStartDate(), hotelInfoRequestDto.getEndDate(),
                hotelInfoRequestDto.getRoomsCount(), daysCount);

        List<RoomPriceResponseDto> rooms = roomPriceDtoList.stream()
                .map(roomPriceDto -> {
                    RoomPriceResponseDto roomPriceResponseDto = modelMapper.map(roomPriceDto.getRoom(),
                            RoomPriceResponseDto.class);
                    roomPriceResponseDto.setPrice(roomPriceDto.getPrice());
                    return roomPriceResponseDto;
                })
                .collect(Collectors.toList());

        return new HotelInfoDto(modelMapper.map(hotel, HotelDTO.class), rooms);
    }
}
