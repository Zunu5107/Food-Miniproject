package com.sparta.foodtruck.global.custom;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.foodtruck.global.dto.ErrorLoginMessageDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class CustomStaticMethodClass {

    public static void setFailResponse(HttpServletResponse response, ErrorLoginMessageDto errorLoginDto) throws IOException {
        log.info("set Fail Response");
        response.setStatus(401);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        String str = objectMapper.writeValueAsString(errorLoginDto);
        response.getWriter().write(str);
    }
}
