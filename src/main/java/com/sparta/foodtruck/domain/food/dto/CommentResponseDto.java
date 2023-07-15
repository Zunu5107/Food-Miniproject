package com.sparta.foodtruck.domain.food.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CommentResponseDto {

    private List<CommentDto> commentList;


}
