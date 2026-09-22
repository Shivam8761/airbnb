package com.shivam.airBnb.entity;

import com.shivam.airBnb.dto.RoomDTO;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
//@ToString
@Table(name = "hotel")
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;

    private String city;

    @Column(columnDefinition = "TEXT[]")
    private String[] photos ;

    @Column(columnDefinition = "TEXT[]")
    private String[] amenities ;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private Boolean active;

    @Embedded
    private HotelContactInfo hotelContactInfo ;

    @ManyToOne
    private User owner;

    @OneToMany(mappedBy = "hotel")
    private List<Room> rooms;
}

//har ek entity ke upr @column lagana zaruri nahi h , ye kaam hibernate automatically kar deta h but hme kuchh specify karana h , jaise nullable to hm karate h !!
