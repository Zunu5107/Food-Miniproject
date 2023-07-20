package com.sparta.foodtruck.domain.accountinfo.controller;

import com.sparta.foodtruck.domain.accountinfo.dto.IntroduceResponseDto;
import com.sparta.foodtruck.domain.accountinfo.dto.ModifiedIntroduceDto;
import com.sparta.foodtruck.domain.accountinfo.service.UserAccountService;
import com.sparta.foodtruck.domain.user.sercurity.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
public class UserAccountController {
    private final UserAccountService userAccountService;

    @GetMapping("/introduce")
    public ResponseEntity<IntroduceResponseDto> introduce(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return userAccountService.introduce(userDetails);
    }

    @PatchMapping("/introduce")
    public ResponseEntity<IntroduceResponseDto> introduceModified(@RequestBody ModifiedIntroduceDto responseDto,
                                                                     @AuthenticationPrincipal UserDetailsImpl userDetails){
        return userAccountService.introduceModified(responseDto, userDetails);
    }
}
