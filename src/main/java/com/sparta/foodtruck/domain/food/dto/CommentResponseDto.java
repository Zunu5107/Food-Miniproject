package com.sparta.foodtruck.domain.food.dto;

import com.sparta.foodtruck.domain.food.entity.FoodComment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CommentResponseDto {

    private String username;
    private String content;

    public CommentResponseDto(String username, String content) {
        this.username = username;
        this.content = content;
    }

    public CommentResponseDto(FoodComment foodComment){
        this.username = foodComment.getUsername();
        this.content = foodComment.getContent();
    }

}
