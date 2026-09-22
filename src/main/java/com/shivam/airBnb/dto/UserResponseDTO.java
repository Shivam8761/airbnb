package com.shivam.airBnb.dto;


import com.shivam.airBnb.entity.enums.Gender;
import com.shivam.airBnb.entity.enums.Role;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String password;
    private Gender gender;
    private LocalDate dateOfBirth;
    private Set<Role> roles;
}
