package com.shivam.airBnb.repository;

import com.shivam.airBnb.entity.Guest;
import com.shivam.airBnb.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GuestRepository extends JpaRepository<Guest, Long> {
    List<Guest> findByUser(User user);
}
