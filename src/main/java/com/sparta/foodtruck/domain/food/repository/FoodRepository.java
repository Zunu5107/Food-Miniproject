package com.sparta.foodtruck.domain.food.repository;

import com.sparta.foodtruck.domain.food.dto.CreateFoodRequestDto;
import com.sparta.foodtruck.domain.food.entity.Food;
import com.sparta.foodtruck.domain.food.entity.FoodValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FoodRepository extends JpaRepository<Food, Long> {


//    List<Food> findAllBySaltyAndAndSpicyAndAndWorldAndAndHotOrderByFoodCountDesc(boolean salty, int spicy, int world, boolean hot);

//    @Modifying
//    @Query("update Food food set food.foodCount = food.foodCount + 1 where food.id = :id")
//    int updateFoodCount(Long id);
//    @Query("SELECT f FROM Food f ORDER BY f.foodCount DESC")
//    Page<Food> findAllOrderByFoodCountDesc(Pageable pageable);
}
