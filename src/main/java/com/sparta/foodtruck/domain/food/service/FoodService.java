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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    private final Long MAX_FOOD = 82L;


    public ResponseEntity<CustomStatusResponseDto> createFood(CreateFoodRequestDto requestDto) {
        Food newFood = new Food(requestDto.getFoodName(), requestDto.getImageUrl());
        newFood = foodRepository.save(newFood);

        FoodValue newFoodValue = new FoodValue(newFood, requestDto.getSalty(), requestDto.getHot(), requestDto.getSpicy(), FoodValue.FoodWorldValue.findByNumber(requestDto.getWorld()));
        foodValueRepository.save(newFoodValue);

        return ResponseEntity.ok(new CustomStatusResponseDto(true));
    }


    public ResponseEntity<List<FoodResponseDto>> resultFood(FoodRequestDto requestDto) {
        List<Long> findFood;
        if (requestDto.getWorld() == 0) {
            findFood = queryFactory
                    .select(foodValue.id)
                    .from(foodValue)
                    .where(foodValue.salty.eq(requestDto.isSalty()),
                            foodValue.maxSpicy.goe(requestDto.getSpicy()),
                            foodValue.minSpicy.loe(requestDto.getSpicy()),
                            foodValue.hot.eq(requestDto.isHot()))
                    .fetch();
        } else {
            findFood = queryFactory
                    .select(foodValue.id)
                    .from(foodValue)
                    .where(foodValue.salty.eq(requestDto.isSalty()),
                            foodValue.maxSpicy.goe(requestDto.getSpicy()),
                            foodValue.minSpicy.loe(requestDto.getSpicy()),
                            foodValue.world.eq(FoodValue.FoodWorldValue.findByNumber(requestDto.getWorld())),
                            foodValue.hot.eq(requestDto.isHot()))
                    .fetch();
        }

        Collections.shuffle(findFood);
        while (findFood.size() < 4) {
            Long Index = randomIndex();
            if (!findFood.contains(Index)) {
                findFood.add(Index);
            }
        }
        findFood = findFood.subList(0, 4);

        return ResponseEntity.ok(getResult(findFood).stream().map(FoodResponseDto::new).toList());
    }

    private Long randomIndex() {
        return (long) (Math.random() * (MAX_FOOD + 1));
    }

    private List<Food> getResult(List<Long> num) {
        List<Food> resultFood = queryFactory.select(foodValue.food).from(foodValue).where(foodValue.id.in(num)).fetch();
        Collections.shuffle(resultFood);
        return resultFood;
    }

    public List<FoodResponseDto> getRandomResult() {
        List<Long> initNumber = new ArrayList<>();
        while (initNumber.size() < 4) {
            Long Index = randomIndex();
            if (!initNumber.contains(Index)) {
                initNumber.add(Index);
            }
        }
        return getResult(initNumber).stream().map(FoodResponseDto::new).toList();
    }

    public List<FoodResponseDto> getFoodRank() {
        Pageable pageable = PageRequest.of(0, 5); // 5개씩 끊어서(1,2,3,4,5까지 조회)
        Page<Food> foodList = foodRepository.findAllByOrderBySelectByDesc(pageable);

        return foodList.map(FoodResponseDto::new).stream().toList();
    }

    @Transactional
    public CustomStatusResponseDto choiceFood(Long foodId) {
        Long sel = queryFactory.select(food.selectBy).from(food).where(food.id.eq(foodId)).fetchOne();
        queryFactory.update(food).set(food.selectBy, ++sel).where(food.id.eq(foodId)).execute();
        return new CustomStatusResponseDto(true);
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
            } else
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

    @Transactional
    public ResponseEntity<List<CommentResponseDto>> addCommentDebugTwo(Long foodId, UserDetailsImpl userDetails, CommentRequestDebugDto requestDto) {
        Food food = findFood(foodId);

        FoodComment foodComment = new FoodComment(food, userDetails.getAccountInfo(), requestDto.getContent());
        foodCommentRepository.save(foodComment);

        return ResponseEntity.status(201).body(getCommentByFood(foodId));
    }

    // 이게 어차피 food 조회해 주는 거잖아 넹

    private List<CommentResponseDto> getCommentByFood(Long foodId) {
        List<FoodComment> foodCommentList = queryFactory.selectFrom(foodComment).where(foodComment.food.id.eq(foodId)).orderBy(foodComment.id.desc()).fetch();
        return foodCommentList.stream().map(CommentResponseDto::new).toList();
    }

    public ResponseEntity<CommentListResponseDto> getCommentByFood(Long foodId, UserDetailsImpl userDetails) {
        List<CommentResponseDto> foodCommentList = getCommentByFood(foodId);
        CommentListResponseDto responseDto = new CommentListResponseDto();
        responseDto.setData(foodCommentList);
        if (userDetails != null) {
            Long id = queryFactory.select(foodLike.accountInfo.id).from(foodLike).where(foodLike.food.id.eq(foodId),foodLike.accountInfo.id.eq(userDetails.getAccountInfo().getId())).fetchOne();
            responseDto.setUserLike((id != null ? true : false));
        } else
            responseDto.setUserLike(false);
        return ResponseEntity.ok(responseDto);
    }

    @Transactional
    public List<CommentResponseDto> patchCommentByFood(Long foodId, UserDetailsImpl userDetails, Long commentId, CommentRequestDebugDto requestDto) {
        checkComment(foodId, commentId, userDetails);
        queryFactory.update(foodComment).set(foodComment.content, requestDto.getContent()).where(foodComment.id.eq(commentId)).execute();

        return getCommentByFood(foodId);
    }

    @Transactional
    public ResponseEntity<CustomStatusResponseDto> DeleteCommentByFood(Long foodId, Long commentId, UserDetailsImpl userDetails) {
        log.info("enter");

        checkComment(foodId, commentId, userDetails);

        queryFactory.delete(foodComment).where(foodComment.id.eq(commentId)).execute();

        return ResponseEntity.ok(new CustomStatusResponseDto(true));
    }

    private void checkComment(Long foodId, Long commentId, UserDetailsImpl userDetails) {
        log.info("enter check");
        Long accountId = queryFactory.select(foodComment.accountInfo.id).from(foodComment).where(foodComment.food.id.eq(foodId), foodComment.id.eq(commentId)).fetchOne();
        if (accountId == null)
            throw CustomStatusException.builder("해당 내용이 존재하지 않습니다.").status(404).build();
        if (accountId != userDetails.getUser().getId()) {
            throw CustomStatusException.builder("사용자가 일치 하지 않습니다.").status(401).build();
        }
    }

}