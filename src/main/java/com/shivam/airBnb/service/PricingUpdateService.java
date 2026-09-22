package com.shivam.airBnb.service;


import com.shivam.airBnb.entity.Hotel;
import com.shivam.airBnb.entity.HotelMinPrice;
import com.shivam.airBnb.entity.Inventory;
import com.shivam.airBnb.repository.HotelMinPriceRepository;
import com.shivam.airBnb.repository.HotelRepository;
import com.shivam.airBnb.repository.InventoryRepository;
import com.shivam.airBnb.strategy.PricingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PricingUpdateService {
    private final HotelRepository hotelRepository;
    private final InventoryRepository inventoryRepository;
    private final PricingService pricingService;
    private final HotelMinPriceRepository hotelMinPriceRepository;

    @Scheduled(cron = " 0 0 * * * *")
    public void updatePrice(){
        int page = 0;
        int BatchSize = 100;

        while(true) {
            Page<Hotel> hotelPage = hotelRepository.findAll(PageRequest.of(page,BatchSize));

            if(hotelPage.isEmpty()){
                break;
            }
            else{
                hotelPage.getContent().forEach(hotel -> updateHotelPrice(hotel)) ;
            }
        }
    }

    private void updateHotelPrice(Hotel hotel){
        LocalDate startDate = LocalDate.now() ;
        LocalDate endDate = startDate.plusDays(1);

        List<Inventory> inventoryList = inventoryRepository.findByHotelAndDateBetween(hotel ,startDate ,endDate);

        updateInventoryPrice(inventoryList) ;

        updateHotelMinPrice(hotel,inventoryList , startDate ,endDate);
    }

    private void updateHotelMinPrice(Hotel hotel, List<Inventory> inventoryList,LocalDate startDate,LocalDate endDate){

        // Compute minimum price per day for the hotel
        Map<LocalDate, BigDecimal> dailyMinPrices = inventoryList.stream()
                .collect(Collectors.groupingBy(
                        Inventory::getDate,
                        Collectors.mapping(Inventory::getPrice, Collectors.minBy(Comparator.naturalOrder()))
                ))
                .entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().orElse(BigDecimal.ZERO)));

        // Prepare HotelPrice entities in bulk
        List<HotelMinPrice> hotelPrices = new ArrayList<>();
        dailyMinPrices.forEach((date, price) -> {
            HotelMinPrice hotelPrice = hotelMinPriceRepository.findByHotelAndDate(hotel, date)
                    .orElse(new HotelMinPrice(hotel, date));
            hotelPrice.setPrice(price);
            hotelPrices.add(hotelPrice);
        });

        // Save all HotelPrice entities in bulk
        hotelMinPriceRepository.saveAll(hotelPrices);
    }

    private void updateInventoryPrice(List<Inventory> inventoryList){

        inventoryList.forEach(inventory -> {
            BigDecimal dynamicPrice = pricingService.calculateDynamicPricing(inventory);
            inventory.setPrice(dynamicPrice);
        });
        inventoryRepository.saveAll(inventoryList);
    }
}
