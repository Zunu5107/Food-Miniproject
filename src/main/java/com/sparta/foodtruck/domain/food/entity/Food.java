package com.sparta.foodtruck.domain.food.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String foodName;

    String imageUrl;

    Long selectBy;

    UUID uuid;

//    @OneToOne
//    FoodValue foodValue;

    // 단짠
    boolean salty;
    // 맵기
    int spicy; // 1,2,3
    // 나라별 음식
    int world;
    // 뜨거운거
    boolean hot;

    // 조회수
    int foodCount;

    // 좋아요
    int foodLike;

    public Food(String foodName, String imageUrl) {
        this.foodName = foodName;
        this.imageUrl = imageUrl;
        this.selectBy = 0L;
    }

}
