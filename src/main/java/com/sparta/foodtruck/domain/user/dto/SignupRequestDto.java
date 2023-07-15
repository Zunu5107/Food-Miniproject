package com.sparta.foodtruck.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignupRequestDto {

    private String address;
    private String password;
    private String username;

}
