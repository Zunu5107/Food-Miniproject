package com.sparta.foodtruck.domain.user.controller;

import com.sparta.foodtruck.domain.user.dto.SignupRequestDto;
import com.sparta.foodtruck.domain.user.service.UserService;
import com.sparta.foodtruck.global.dto.CustomStatusResponseDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<CustomStatusResponseDto> createAccount(@RequestBody SignupRequestDto requestDto){
        return userService.createAccount(requestDto);
    }
}
