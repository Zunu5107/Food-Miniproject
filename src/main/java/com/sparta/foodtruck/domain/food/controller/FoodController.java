package com.sparta.foodtruck.domain.food.controller;

import com.sparta.foodtruck.domain.food.dto.*;
import com.sparta.foodtruck.domain.food.entity.Food;
import com.sparta.foodtruck.domain.food.entity.FoodValue;
import com.sparta.foodtruck.domain.food.service.FoodService;
import com.sparta.foodtruck.global.dto.CustomStatusResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/food")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;

    @PostMapping
    public ResponseEntity<CustomStatusResponseDto> createFood(@RequestBody CreateFoodRequestDto requestDto){
        return foodService.createFood(requestDto);
    }

    /**
     *
     * @param requestDto
     * {
     * gender: true | false
     * salty: true | false
     * spicy: (0 - 4)
     * world : (0 - 4)
     * hot = true | false
     * }
     * @return
     * foodList : [
     * {
     * id : Number
     * name: “foodName”,
     * ImageUrl : “url”
     * comment : []
     * } ….
     */
    @PostMapping("/result")
    public ResponseEntity<List<FoodResponseDto>> resultFood(@RequestBody FoodRequestDto requestDto) {
        return foodService.resultFood(requestDto);
    }

    @PutMapping("/{foodId}/choice")
    public CustomStatusResponseDto choiceFood(@PathVariable Long foodId,
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

//    @PostMapping("/{foodId}/comment")
//    public ResponseEntity<CommentResponseDto> addComment(@PathVariable Long foodId,
//                                                         @RequestBody CommentRequestDto requestDto) {
//        List<CommentResponseDto> commentList = foodService.addComment();
//        return ResponseEntity.ok();
//    }

    @GetMapping("/{foodId}/comment")
    public List<CommentResponseDto> getCommentByFood(@PathVariable Long foodId) {
        return foodService.getCommentByFood(foodId);
    }
}
