package com.shivam.airBnb.dto;


import com.shivam.airBnb.entity.Hotel;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomDTO {

    private Long id;
    private String type;

    private BigDecimal basePrice;

    private String[] photos ;

    private String[] amenities ;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Integer totalCount ;

    private Integer capacity;
}
