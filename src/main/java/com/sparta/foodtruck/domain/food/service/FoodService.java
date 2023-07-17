package com.sparta.foodtruck.domain.food.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.foodtruck.domain.food.dto.*;
import com.sparta.foodtruck.domain.food.entity.*;
import com.sparta.foodtruck.domain.food.repository.FoodCommentRepository;
import com.sparta.foodtruck.domain.food.repository.FoodLikeRepository;
import com.sparta.foodtruck.domain.food.repository.FoodRepository;
import com.sparta.foodtruck.domain.food.repository.FoodValueRepository;
import com.sparta.foodtruck.domain.user.entity.AccountInfo;
import com.sparta.foodtruck.domain.user.repository.AccountInfoRepository;
import com.sparta.foodtruck.domain.user.sercurity.UserDetailsImpl;
import com.sparta.foodtruck.global.dto.CustomStatusResponseDto;
import com.sparta.foodtruck.global.exception.CustomStatusException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.sparta.foodtruck.domain.food.entity.QFood.food;
import static com.sparta.foodtruck.domain.food.entity.QFoodComment.foodComment;
import static com.sparta.foodtruck.domain.food.entity.QFoodLike.foodLike;
import static com.sparta.foodtruck.domain.food.entity.QFoodValue.foodValue;


@Slf4j
@Service
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepository;
    private final FoodLikeRepository foodLikeRepository;
    private final FoodValueRepository foodValueRepository;
    private final AccountInfoRepository accountInfoRepository;
    private final JPAQueryFactory queryFactory;
    private final FoodCommentRepository foodCommentRepository;


    public ResponseEntity<CustomStatusResponseDto> createFood(CreateFoodRequestDto requestDto) {
        Food newFood = new Food(requestDto.getFoodName(), requestDto.getImageUrl());
        newFood = foodRepository.save(newFood);

        //FoodValue(Long id, Food food, Boolean salty, Boolean hot, Integer spicy, FoodWorldValue world)
        FoodValue newFoodValue = new FoodValue(newFood, requestDto.getSalty(), requestDto.getHot(), requestDto.getSpicy(), FoodValue.FoodWorldValue.findByNumber(requestDto.getWorld()));
        foodValueRepository.save(newFoodValue);

        return ResponseEntity.ok(new CustomStatusResponseDto(true));
    }



    public ResponseEntity<List<FoodResponseDto>> resultFood(FoodRequestDto requestDto) {

        List<Food> findFood = queryFactory
                .select(foodValue.food)
                .from(foodValue)
                .where(foodValue.salty.eq(requestDto.isSalty()),
                        foodValue.world.eq(FoodValue.FoodWorldValue.findByNumber(requestDto.getWorld())),
                        foodValue.hot.eq(requestDto.isHot()))
                .fetch();
//        List<Food> foodList = findFood.stream().map(e -> e.getFood()).toList();

        return ResponseEntity.ok(findFood.stream().map(FoodResponseDto::new).toList());
    }


    public List<FoodResponseDto> getRandomResult() {
        List<Long> initNumber = new ArrayList<>();
        int i = 4;
        while(i --> 0)
            initNumber.add((long)(Math.random() * 83));
        List<Food> resultFood = queryFactory.select(foodValue.food).from(foodValue).where(foodValue.id.in(initNumber)).fetch();
        Collections.shuffle(resultFood);
        return resultFood.stream().map(FoodResponseDto::new).toList();
    }

    public List<FoodResponseDto> getFoodRank() {
//        Pageable pageable = PageRequest.of(0, 5); // 5개씩 끊어서(1,2,3,4,5까지 조회)
//        Page<Food> foodList = foodRepository.findAllOrderByFoodCountDesc(pageable);
        List<FoodResponseDto> foodResponseDto = queryFactory.selectFrom(food).fetch().stream().map(FoodResponseDto::new).toList();
//        for (Food food : foodList) {
//            foodResponseDto.add(new FoodResponseDto(food));
//        }
        return foodResponseDto;
    }

    @Transactional
    public CustomStatusResponseDto choiceFood(Long foodId, String foodName, FoodRequestDto requestDto, UUID uuid) {
        Food food = findFood(foodId);

        TempResult tempResult = new TempResult();
        tempResult.generateUUID();
        tempResult.getSelectFood();

        if(LocalDateTime.now().isAfter(tempResult.getLocalDateTime()))
            return new CustomStatusResponseDto(false);

        if(food.getFoodName().equals(foodName)) {

            foodRepository.save(food);
            return new CustomStatusResponseDto(true);
        }
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

    public Long likeFood(Long foodId, UserDetailsImpl userDetails) {
        Food food = findFood(foodId);
        AccountInfo accountInfo = userDetails.getAccountInfo();

        if (accountInfo != null) {
            FoodLike foodLike = foodLikeRepository.findByAccountInfoAndFood(accountInfo, food);
            if (foodLike == null) { // 처음 누르면
                foodLike = new FoodLike(accountInfo, food);
                foodLikeRepository.save(foodLike); // 좋아요 저장
            }
            else
                foodLikeRepository.delete(foodLike); // 좋아요 삭제
        }
        return (long) queryFactory.selectFrom(foodLike).where(foodLike.food.id.eq(foodId)).fetch().size();
    }


    public ResponseEntity<List<CommentResponseDto>> addComment(Long foodId, UserDetailsImpl userDetails, CommentRequestDto requestDto) {
        Food food = findFood(foodId);

        // foodComment food / account = null / account.username / content
        FoodComment foodComment = new FoodComment(food, userDetails.getAccountInfo(), requestDto.getContent());
        foodCommentRepository.save(foodComment);
        // 테스트 ㄱㄱ
        return ResponseEntity.status(201).body(getCommentByFood(foodId));
    }
    @Transactional
    public ResponseEntity<List<CommentResponseDto>> addCommentDebug(Long foodId, CommentRequestDebugDto requestDto) {
        Food food = findFood(foodId);

        // foodComment food / account = null / account.username / content
        FoodComment foodComment = new FoodComment(food, requestDto.getUsername(), requestDto.getContent());
        foodCommentRepository.save(foodComment);
        // 테스트 ㄱㄱ
        return ResponseEntity.status(201).body(getCommentByFood(foodId));
    }

    // 이게 어차피 food 조회해 주는 거잖아 넹

    public List<CommentResponseDto> getCommentByFood(Long foodId) {
        List<FoodComment> foodCommentList = queryFactory.selectFrom(foodComment).where(foodComment.food.id.eq(foodId)).orderBy(foodComment.id.desc()).fetch();
        return foodCommentList.stream().map(CommentResponseDto::new).toList();
    }

    @Transactional
    public List<CommentResponseDto> patchCommentByFood(Long foodId, UserDetailsImpl userDetails,Long commentId, CommentRequestDebugDto requestDto) {
        checkComment(foodId,commentId,userDetails);
        queryFactory.update(foodComment).set(foodComment.content, requestDto.getContent()).where(foodComment.id.eq(commentId)).execute();

        return getCommentByFood(foodId);
    }

    @Transactional
    public ResponseEntity<CustomStatusResponseDto> DeleteCommentByFood(Long foodId, Long commentId, UserDetailsImpl userDetails) {
        log.info("enter");

        checkComment(foodId,commentId,userDetails);

        queryFactory.delete(foodComment).where(foodComment.id.eq(commentId)).execute();

        return ResponseEntity.ok(new CustomStatusResponseDto(true));
    }

    private void checkComment(Long foodId, Long commentId, UserDetailsImpl userDetails){
        log.info("enter check");
        Long accountId = queryFactory.select(foodComment.accountInfo.id).from(foodComment).where(foodComment.food.id.eq(foodId), foodComment.id.eq(commentId)).fetchOne();
        if(accountId == null)
            throw CustomStatusException.builder("해당 내용이 존재하지 않습니다.").status(404).build();
        if(accountId != userDetails.getUser().getId()){
            throw CustomStatusException.builder("사용자가 일치 하지 않습니다.").status(401).build();
        }
    }

}