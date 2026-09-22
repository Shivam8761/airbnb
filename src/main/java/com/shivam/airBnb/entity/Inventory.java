package com.shivam.airBnb.entity;

import com.sun.jdi.IntegerValue;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.sql.results.graph.Fetch;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(
        uniqueConstraints = @UniqueConstraint(
                name = "unique_hotel_room_date",
                columnNames = {"hotel_id","room_id" ,"date"}
        )
)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room ;

    @Column(nullable = false)
    private LocalDate date ;

    @Column(nullable = false)
    private String city ;

    @Column(nullable = false,columnDefinition = "Integer Default 0")
    private Integer bookedCount ;

    @Column(nullable = false,columnDefinition = "Integer Default 0")
    private Integer reservedCount ;

    @CreationTimestamp
    @Column(nullable = false,updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(nullable = false, columnDefinition = "Integer default 0")
    private Integer totalCount ;

    @Column(nullable = false,precision = 5,scale = 2)
    private BigDecimal surgeFactor ;

    @Column(nullable = false,precision = 10,scale = 2)
    private BigDecimal price ;

    @Column(nullable = false)
    private Boolean closed;
}
