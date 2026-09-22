package com.shivam.airBnb.controller;


import com.shivam.airBnb.dto.LoginRequestDTO;
import com.shivam.airBnb.dto.LoginResponseDTO;
import com.shivam.airBnb.dto.UserResponseDTO;
import com.shivam.airBnb.security.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signUp")
    @Operation(summary = "Create a new account", tags = {"Auth"})
    public ResponseEntity<UserResponseDTO> signUp(@RequestBody UserResponseDTO userResponseDTO){
            log.info("SignUp Called");
            UserResponseDTO userResponseDTO1 = authService.signUp(userResponseDTO);
            return  new ResponseEntity<>(userResponseDTO1, HttpStatus.OK);
    }

    @PostMapping("/login")
    @Operation(summary = "Login request", tags = {"Auth"})
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO, HttpServletRequest  request, HttpServletResponse response ){
        String[] tokens = authService.login(loginRequestDTO) ;

        Cookie cookie = new Cookie("refreshToken", tokens[1]);

        cookie.setHttpOnly(true);

        response.addCookie(cookie);

        return  ResponseEntity.ok(new LoginResponseDTO(tokens[0]));
    }
    @PostMapping("/refresh")
    @Operation(summary = "Refresh the JWT with a refresh token", tags = {"Auth"})
    public ResponseEntity<LoginResponseDTO> refresh(HttpServletRequest request) {
        String refreshToken = Arrays.stream(request.getCookies()).
                filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new AuthenticationServiceException("Refresh token not found inside the Cookies"));

        String accessToken = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(new LoginResponseDTO(accessToken));
    }
}
