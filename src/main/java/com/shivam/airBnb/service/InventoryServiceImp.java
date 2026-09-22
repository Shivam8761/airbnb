package com.shivam.airBnb.service;

import com.shivam.airBnb.entity.Inventory;
import com.shivam.airBnb.entity.Room;
import com.shivam.airBnb.entity.User;
import com.shivam.airBnb.exceptions.ResourceNotFoundException;
import com.shivam.airBnb.repository.HotelMinPriceRepository;
import com.shivam.airBnb.repository.InventoryRepository;
import com.shivam.airBnb.repository.RoomRepository;
import com.shivam.airBnb.dto.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import static com.shivam.airBnb.utility.AppUtils.getCurrentUser;

@Service
@Slf4j
@RequiredArgsConstructor
public class InventoryServiceImp implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final RoomRepository roomRepository;
    private final ModelMapper modelMapper;
    private final HotelMinPriceRepository hotelMinPriceRepository ;

    @Override
    public void initializeRoomForAYear(Room room) {
        log.info(" i am in initializeRoomForAYear");
        LocalDate today =  LocalDate.now();
        LocalDate endDate = today.plusYears(1) ;

        for( ; !today.isAfter(endDate); today = today.plusDays(1)){
            Inventory inventory = Inventory.builder()
                    .hotel(room.getHotel())
                    .room(room)
                    .city(room.getHotel().getCity())
                    .bookedCount(0)
                    .reservedCount(0)
                    .price(room.getBasePrice())
                    .surgeFactor(BigDecimal.ONE)
                    .date(today)
                    .totalCount(room.getTotalCount())
                    .closed(false)
                    .build() ;

            inventoryRepository.save(inventory);

        }
    }

    @Override
    public void deleteAllInventories(Room room) {
        LocalDate today =  LocalDate.now();
        inventoryRepository.deleteByRoom(room);

    }

    @Override
    public Page<HotelPriceResponseDTO> searchHotel(HotelSearchRequest hotelSearchRequest) {

        Pageable pageable = PageRequest.of(hotelSearchRequest.getPage(), hotelSearchRequest.getSize());

        long dayCount = ChronoUnit.DAYS.between(hotelSearchRequest.getStartDate() ,hotelSearchRequest.getEndDate()) +1 ;

        Page<HotelPriceDTO> hotelPage = hotelMinPriceRepository.findHotelsWithAvailableInventory(hotelSearchRequest.getCity(),
                hotelSearchRequest.getStartDate(),hotelSearchRequest.getEndDate()
                ,hotelSearchRequest.getRoomsCount(),dayCount,pageable);

        return hotelPage.map((hotelPriceDTO)->{
            HotelPriceResponseDTO hotelPriceResponseDTO = modelMapper.map(hotelPriceDTO.getHotel(),HotelPriceResponseDTO.class);
            hotelPriceResponseDTO.setPrice(hotelPriceDTO.getPrice());
            return hotelPriceResponseDTO;
                });
    }

    @Override
    public List<InventoryDto> getAllInventoryByRoom(Long roomId) {
        log.info("Getting All inventory by room for room with id: {}", roomId);
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: "+roomId));

        User user = getCurrentUser();
        if(!user.equals(room.getHotel().getOwner())) throw new AccessDeniedException("You are not the owner of room with id: "+roomId);

        return inventoryRepository.findByRoomOrderByDate(room).stream()
                .map((element) -> modelMapper.map(element,
                        InventoryDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateInventory(Long roomId, UpdateInventoryRequestDto updateInventoryRequestDto) {
        log.info("Updating All inventory by room for room with id: {} between date range: {} - {}", roomId,
                updateInventoryRequestDto.getStartDate(), updateInventoryRequestDto.getEndDate());

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: "+roomId));

        User user = getCurrentUser();
        if(!user.equals(room.getHotel().getOwner())) throw new AccessDeniedException("You are not the owner of room with id: "+roomId);

        inventoryRepository.getInventoryAndLockBeforeUpdate(roomId, updateInventoryRequestDto.getStartDate(),
                updateInventoryRequestDto.getEndDate());

        inventoryRepository.updateInventory(roomId, updateInventoryRequestDto.getStartDate(),
                updateInventoryRequestDto.getEndDate(), updateInventoryRequestDto.getClosed(),
                updateInventoryRequestDto.getSurgeFactor());
    }
}
