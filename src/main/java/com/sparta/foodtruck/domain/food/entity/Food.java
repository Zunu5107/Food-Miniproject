package com.sparta.foodtruck.domain.food.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String foodName;

    String imageUrl;

    Long selectBy;

    @OneToOne
    FoodValue foodValue;

    public Food(String foodName, String imageUrl) {
        this.foodName = foodName;
        this.imageUrl = imageUrl;
        this.selectBy = 0L;
    }
}
