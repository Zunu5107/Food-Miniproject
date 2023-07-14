package com.sparta.foodtruck.domain.user.repository;

import com.sparta.foodtruck.domain.user.entity.AccountInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountInfoRepository extends JpaRepository<AccountInfo, Long> {
}
