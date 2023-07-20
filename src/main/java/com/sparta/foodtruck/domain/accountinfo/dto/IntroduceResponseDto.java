package com.sparta.foodtruck.domain.accountinfo.dto;

import com.sparta.foodtruck.domain.food.dto.FoodResponseDto;
import com.sparta.foodtruck.domain.food.entity.Food;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class IntroduceResponseDto {
    private String address;
    private String username;
    private String introduce;
    private String profileImage;
    private List<FoodResponseDto> myLike;

    public IntroduceResponseDto(String id, String username, String introduce, String profileImage,List<Food> foodList) {
        this.address = id;
        this.username = username;
        this.introduce = introduce;
        this.profileImage = profileImage;
        this.myLike = foodList.stream().map(FoodResponseDto::new).toList();
    }
}

/*
{
id:””
username: “ username”
introduce : “”
}
 */