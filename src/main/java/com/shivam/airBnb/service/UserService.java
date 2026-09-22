package com.shivam.airBnb.service;

import com.shivam.airBnb.entity.User;
import com.shivam.airBnb.dto.ProfileUpdateRequestDto;
import com.shivam.airBnb.dto.UserResponseDTO;

public interface UserService {

    User getUserById(Long id);

    void updateProfile(ProfileUpdateRequestDto profileUpdateRequestDto);

    UserResponseDTO getMyProfile();
}
