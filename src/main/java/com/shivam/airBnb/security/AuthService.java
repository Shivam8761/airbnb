package com.shivam.airBnb.security;


import com.shivam.airBnb.entity.User;
import com.shivam.airBnb.entity.enums.Role;
import com.shivam.airBnb.exceptions.ResourceNotFoundException;
import com.shivam.airBnb.repository.UserRepository;
import com.shivam.airBnb.dto.LoginRequestDTO;
import com.shivam.airBnb.dto.LoginResponseDTO;
import com.shivam.airBnb.dto.UserRequestDTO;
import com.shivam.airBnb.dto.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final ModelMapper modelMapper ;
    private final PasswordEncoder passwordEncoder ;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager ;

    public UserResponseDTO signUp(UserResponseDTO userResponseDTO) {

        User user = userRepository.findByEmail(userResponseDTO.getEmail()).orElse(null);
        log.info("User : {}" ,user);

        if(user != null){
            throw new ResourceNotFoundException("User with email " + userResponseDTO.getEmail() + " already exists");
        }

        User newUser = modelMapper.map(userResponseDTO, User.class);

        newUser.setRoles(Set.of(Role.GUEST));
        newUser.setPassword(passwordEncoder.encode(userResponseDTO.getPassword()));

        User newUser1 = userRepository.save(newUser);

        return modelMapper.map(newUser1, UserResponseDTO.class);
    }

    public String[] login(LoginRequestDTO loginRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequestDTO.getEmail() , loginRequestDTO.getPassword()
        )) ;

        User user = (User) authentication.getPrincipal();


        String[] arr = new String [2];

        arr[0] = jwtService.generateAccessToken(user);
        arr[1] = jwtService.generateRefreshToken(user);

        return arr;
    }

    public String refreshToken(String refreshToken) {
          Long id = jwtService.getUserIdFromToken(refreshToken);

          User user = userRepository.findById(id)
                  .orElseThrow(()-> new ResourceNotFoundException("User with id " + id + " not found"));

          return jwtService.generateAccessToken(user);
    }
}
