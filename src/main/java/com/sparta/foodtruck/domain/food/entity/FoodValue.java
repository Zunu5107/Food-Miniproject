package com.sparta.foodtruck.domain.food.entity;

import com.sparta.foodtruck.domain.food.dto.FoodRequestDto;
import com.sparta.foodtruck.domain.food.dto.FoodResponseDto;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor//(access = AccessLevel.PROTECTED)
public class FoodValue {
    @Id
    Long id;

    @OneToOne
    Food food;

    Boolean salty;

    Boolean hot;

    Integer spicy;

    FoodWorldValue world;

    public FoodValue(Long id, Food food, Boolean salty, Boolean hot, Integer spicy, FoodWorldValue world) {
        this.id = id;
        this.food = food;
        this.salty = salty;
        this.hot = hot;
        this.spicy = spicy;
        this.world = world;
    }


    public enum FoodWorldValue {
        KOREA(1),
        CHINA(2),
        JAPAN(3),
        AMERICA(4);

        private int value;

        FoodWorldValue(int value) {
            this.value = value;
        }

        static FoodWorldValue findByNumber(int num) {
            for (FoodWorldValue foodWorldValue : FoodWorldValue.values()) {
                if (num == foodWorldValue.value) {
                    return foodWorldValue;
                }
            }
            throw new IllegalArgumentException("올바른 숫자가 없습니다.");
        }
    }

    public FoodValue(FoodRequestDto requestDto) {
//        this.gender = requestDto.isGender();
        this.salty = requestDto.isSalty();
        this.spicy = requestDto.getSpicy();
        this.world = FoodWorldValue.findByNumber(requestDto.getWorld());
        this.hot = requestDto.isHot();

    }

}

