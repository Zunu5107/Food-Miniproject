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
import com.sparta.foodtruck.global.dto.CustomStatusResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.sparta.foodtruck.domain.food.entity.QFoodValue.foodValue;


@Service
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepository;
    private final FoodLikeRepository foodLikeRepository;
    private final AccountInfoRepository accountInfoRepository;




    public ResponseEntity<List<FoodResponseDto>> resultFood(FoodRequestDto requestDto) {

        List<Food> findFood = queryFactory
                .select(foodValue.food)
                .from(foodValue)
                .where(foodValue.salty.eq(requestDto.isSalty()),
                        foodValue.spicy.eq(requestDto.getSpicy()),
                        foodValue.world.eq(FoodValue.FoodWorldValue.findByNumber(requestDto.getWorld())),
                        foodValue.hot.eq(requestDto.isHot()))
                .fetch();
//        List<Food> foodList = findFood.stream().map(e -> e.getFood()).toList();

        return ResponseEntity.ok(findFood.stream().map(FoodResponseDto::new).toList());
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
    public CustomStatusResponseDto choiceFood(Long foodId, FoodRequestDto requestDto) {
        Food food = findFood(foodId);

//        if (food.getFoodName().equals(requestDto.getFoodName())) {
//            food.setFoodName(requestDto.getFoodName());
//            return new CustomStatusResponseDto(true);
//        }
        return new CustomStatusResponseDto(false);
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

    public List<CommentResponseDto> addComment(Long foodId, CommentRequestDto requestDto) {
        Food food = findFood(foodId);

        foodRepository.save(food);
        return null;
    }

    public List<CommentResponseDto> getCommentByFood(Long foodId) {
        Food food = findFood(foodId);
        return null;
    }
}


