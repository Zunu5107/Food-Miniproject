package com.sparta.foodtruck.domain.user.dto;

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
    private List<FoodResponseDto> myLike;

    public IntroduceResponseDto(String id, String username, String introduce, List<Food> foodList) {
        this.address = id;
        this.username = username;
        this.introduce = introduce;
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