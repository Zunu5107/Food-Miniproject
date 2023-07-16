package com.sparta.foodtruck;

import com.sparta.foodtruck.global.redis.RedisService;
import com.sparta.foodtruck.global.util.AESUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class FoodTruckApplicationTests {

    @Autowired
    RedisService redisService;

    @Autowired
    AESUtil aesUtil;
    @Test
    void contextLoads() {
    }

    @Test
    void Test1(){
        redisService.setValuesByExpireSecond("hello", "world", 10L);
//        String get = redisService.getValues("hello");
//        System.out.println("get = " + get);
    }

    @Test
    void AESTest(){
        UUID uuid = UUID.randomUUID();
        String token = uuid.toString();
        System.out.println("token = " + token);
        token = aesUtil.encrypt(token);
        System.out.println("token = " + token);
        token = aesUtil.decrypt(token);
        System.out.println("token = " + token);
    }

}
