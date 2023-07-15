package com.sparta.foodtruck.domain.food.service;

import com.sparta.foodtruck.domain.food.dto.CommentRequestDto;
import com.sparta.foodtruck.domain.food.dto.CommentResponseDto;
import com.sparta.foodtruck.domain.food.dto.FoodRequestDto;
import com.sparta.foodtruck.domain.food.dto.FoodResponseDto;
import com.sparta.foodtruck.domain.food.entity.Food;
import com.sparta.foodtruck.domain.food.entity.FoodLike;
import com.sparta.foodtruck.domain.food.repository.FoodLikeRepository;
import com.sparta.foodtruck.domain.food.repository.FoodRepository;
import com.sparta.foodtruck.domain.user.entity.AccountInfo;
import com.sparta.foodtruck.domain.user.repository.AccountInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepository;
    private final FoodLikeRepository foodLikeRepository;
    private final AccountInfoRepository accountInfoRepository;



    public List<FoodResponseDto> resultFood(FoodRequestDto requestDto) {


        List<Food> foodList = foodRepository.findAllBySaltyAndAndSpicyAndAndWorldAndAndHotOrderByFoodCountDesc(requestDto.isSalty(), requestDto.getSpicy(), requestDto.getWorld(), requestDto.isHot());
        List<FoodResponseDto> foodResponseDto = new ArrayList<>();
        for (Food food : foodList) {
            foodResponseDto.add(new FoodResponseDto(food));
            foodRepository.updateFoodCount(food.getId());
        }
        return foodResponseDto;
//    if(requestDto.getSpicy() == 0 || requestDto.getSpicy() == 1) {
//        requestDto.setSpicy(1);

    }

    public List<FoodResponseDto> getFoodRank() {
        Pageable pageable = PageRequest.of(0, 5); // 5개씩 끊어서(1,2,3,4,5까지 조회)
        Page<Food> foodList = foodRepository.findAllOrderByFoodCountDesc(pageable);
        List<FoodResponseDto> foodResponseDto = new ArrayList<>();
        for (Food food : foodList) {
            foodResponseDto.add(new FoodResponseDto(food));
        }
        return foodResponseDto;
    }

    @Transactional
    public boolean choiceFood(Long foodId, FoodRequestDto requestDto) {
        Food food = findFood(foodId);

        if (food.getFoodName().equals(requestDto.getFoodName())) {
            food.setFoodName(requestDto.getFoodName());
            return true;
        }
        return false;
    }

    private Food findFood(Long foodId) {
        return foodRepository.findById(foodId).orElseThrow(() ->
                new IllegalArgumentException("해당하는 음식이 존재하지 않습니다."));
    }

    public boolean likeFood(Long foodId, String token) {

        Food food = findFood(foodId);
        boolean isFirstLike = false;

        AccountInfo accountInfo = accountInfoRepository.findByToken(token); // 사용자 정보 조회

        if (accountInfo != null) {
            FoodLike foodLike = foodLikeRepository.findByAccountInfoAndFood(accountInfo, food); // 사용자가 좋아요를 눌렀는가
            if (foodLike == null) { // 처음 누르면
                foodLike = new FoodLike(accountInfo, food);
                isFirstLike = true;
                foodLikeRepository.save(foodLike); // 좋아요 저장
            }
            foodLikeRepository.delete(foodLike); // 좋아요 삭제
        }
        return isFirstLike;

    }

    public List<CommentResponseDto> addComment(Long foodId, String token, CommentRequestDto requestDto) {
        Food food = findFood(foodId);

        foodRepository.save(food);
        return null;
    }

    public List<CommentResponseDto> getCommentByFood(Long foodId) {
        Food food = findFood(foodId);
        return null;
    }
}


