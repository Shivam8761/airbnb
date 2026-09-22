package com.shivam.airBnb.repository;

import com.shivam.airBnb.entity.Hotel;
import com.shivam.airBnb.entity.HotelMinPrice;
import com.shivam.airBnb.dto.HotelPriceDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface HotelMinPriceRepository extends JpaRepository<HotelMinPrice, Long> {
    @Query("""
        SELECT new com.shivam.airBnb.dto.HotelPriceDTO(i.hotel ,AVG(i.price))
        FROM HotelMinPrice i
        WHERE i.hotel.city = :city
        AND i.date BETWEEN :startDate AND :endDate
        AND i.hotel.active = true
        GROUP BY i.hotel
""")
    Page<HotelPriceDTO> findHotelsWithAvailableInventory(@Param("city") String city,@Param("startDate") LocalDate startDate
            ,@Param("endDate") LocalDate endDate,@Param("roomsCount") Integer roomsCount
            ,@Param("dayCount") long dayCount,@Param("pageable") Pageable pageable);

    ScopedValue<HotelMinPrice> findByHotelAndDate(Hotel hotel, LocalDate date);
}
