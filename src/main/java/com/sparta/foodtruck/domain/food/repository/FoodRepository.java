package com.sparta.foodtruck.domain.food.repository;

import com.sparta.foodtruck.domain.food.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<Food, Long> {

//    List<Food> findAllBySaltyAndAndSpicyAndAndWorldAndAndHotOrderByFoodCountDesc(boolean salty, int spicy, int world, boolean hot);

//    @Modifying
//    @Query("update Food food set food.foodCount = food.foodCount + 1 where food.id = :id")
//    int updateFoodCount(Long id);
//    @Query("SELECT f FROM Food f ORDER BY f.foodCount DESC")
//    Page<Food> findAllOrderByFoodCountDesc(Pageable pageable);
}
