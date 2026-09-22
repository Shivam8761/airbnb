package com.shivam.airBnb.repository;

import com.shivam.airBnb.entity.Hotel;
import com.shivam.airBnb.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
    List<Hotel> findByOwner(User user);
}