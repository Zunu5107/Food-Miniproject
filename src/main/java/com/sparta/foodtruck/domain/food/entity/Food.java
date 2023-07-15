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


    public Food(String foodName, String imageUrl) {
        this.foodName = foodName;
        this.imageUrl = imageUrl;
        this.selectBy = 0L;
    }

}
