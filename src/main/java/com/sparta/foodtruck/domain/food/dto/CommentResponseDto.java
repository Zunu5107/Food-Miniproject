package com.sparta.foodtruck.domain.food.dto;

import com.sparta.foodtruck.domain.food.entity.FoodComment;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
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
