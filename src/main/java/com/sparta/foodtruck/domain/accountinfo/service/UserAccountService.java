package com.sparta.foodtruck.domain.accountinfo.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.sparta.foodtruck.domain.accountinfo.dto.IntroduceResponseDto;
import com.sparta.foodtruck.domain.accountinfo.dto.ModifiedIntroduceDto;
import com.sparta.foodtruck.domain.food.entity.Food;
import com.sparta.foodtruck.domain.user.sercurity.UserDetailsImpl;
import com.sparta.foodtruck.global.dto.CustomStatusResponseDto;
import com.sparta.foodtruck.global.exception.CustomStatusException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.sparta.foodtruck.domain.food.entity.QFoodLike.foodLike;
import static com.sparta.foodtruck.domain.user.entity.QAccountInfo.accountInfo;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAccountService {

    private final JPAQueryFactory queryFactory;

    public ResponseEntity<IntroduceResponseDto> introduce(UserDetailsImpl userDetails) {
        if (userDetails == null)
            throw CustomStatusException.builder("접속 되지 않은 유저입니다.").status(401).build();

        List<Food> likeList = queryFactory.select(foodLike.food).from(foodLike).where(foodLike.accountInfo.id.eq(userDetails.getUser().getId())).fetch();
        return ResponseEntity.ok(new IntroduceResponseDto(userDetails.getUser().getAddress(), userDetails.getUsername(), userDetails.getAccountInfo().getIntroduce(), userDetails.getAccountInfo().getProfileImage(), likeList));
    }

    @Transactional
    public ResponseEntity<IntroduceResponseDto> introduceModified(ModifiedIntroduceDto responseDto, UserDetailsImpl userDetails) {
        if (userDetails == null)
            throw CustomStatusException.builder("접속 되지 않은 유저입니다.").status(401).build();
        if ((responseDto.getIntroduce() == null || responseDto.getIntroduce().isBlank()) && responseDto.getProfileImage() == null)
            return introduce(userDetails);
        JPAUpdateClause queryset = queryFactory.update(accountInfo);
        if (responseDto.getIntroduce() != null || !responseDto.getIntroduce().isBlank()){
            queryset = queryset.set(accountInfo.introduce, responseDto.getIntroduce());
            userDetails.getAccountInfo().modifiedIntroduce(responseDto.getIntroduce());
        }
        if (responseDto.getProfileImage() != null){
            queryset = queryset.set(accountInfo.profileImage, responseDto.getProfileImage());
            userDetails.getAccountInfo().modifiedProfileImage(responseDto.getProfileImage());
        }
        queryset.where(accountInfo.id.eq(userDetails.getAccountInfo().getId())).execute();
        return introduce(userDetails);
    }
}
