package com.sparta.foodtruck.domain.food.repository;

import com.sparta.foodtruck.domain.food.entity.FoodValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FoodValueRepository extends JpaRepository<FoodValue, Long> {


}
