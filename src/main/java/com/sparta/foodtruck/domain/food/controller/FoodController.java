package com.sparta.foodtruck.domain.food.controller;

import com.sparta.foodtruck.domain.food.dto.*;
import com.sparta.foodtruck.domain.food.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/food")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;

    @PostMapping("/result")
    public List<FoodResponseDto> resultFood(@RequestBody FoodRequestDto requestDto) {
        return foodService.resultFood(requestDto);
    }

    @PutMapping("/{foodId}/choice")
    public boolean choiceFood(@PathVariable Long foodId,
                              @RequestBody FoodRequestDto requestDto) {
        return foodService.choiceFood(foodId, requestDto);
    }

    @GetMapping("/rank")
    public List<FoodResponseDto> getFoodRank() {
        return foodService.getFoodRank();
    }

    @PostMapping("/{foodId}/like")
    public boolean likeFood(@PathVariable Long foodId,
                            @RequestHeader("Authorization") String token) {
        return foodService.likeFood(foodId, token);
    }

    @PostMapping("/{foodId}/comment")
    public ResponseEntity<CommentResponseDto> addComment(@PathVariable Long foodId,
                                                         @RequestHeader("Authorization") String token,
                                                         @RequestBody CommentRequestDto requestDto) {
        CommentDto comment = new CommentDto(requestDto.getUsername(), requestDto.getContent());
        foodService.addComment(foodId, token, requestDto);


        return null;
    }

    @GetMapping("/{foodId}/comment")
    public List<CommentResponseDto> getCommentByFood(@PathVariable Long foodId) {
        return foodService.getCommentByFood(foodId);
    }
}
