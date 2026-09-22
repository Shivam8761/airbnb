package com.shivam.airBnb.dto;


import com.shivam.airBnb.entity.enums.Gender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserRequestDTO {
    private Long id;
    private String name;
    private String email;
    private String password;
    private Gender gender;
    private LocalDate dateOfBirth;
}
