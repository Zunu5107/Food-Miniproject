package com.sparta.foodtruck.global.custom;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.foodtruck.global.dto.ErrorLoginMessageDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

@Slf4j
public class CustomStaticMethodClass {

    @Value("${custom.option.originLink}")
    public static String Origin;

    public static void setFailResponse(HttpServletResponse response, ErrorLoginMessageDto errorLoginDto) throws IOException {
//        Collection <String> em = response.getHeaderNames();
//        for (String s : em) {
//            String value = response.getHeader(s);
//            log.info("Header name = " + s);
//            log.info("Header value = " + value);
//        }
        log.info("set Fail Response");
        if (!response.isCommitted()) {
            response.resetBuffer();
        }
        response.setHeader("Access-Control-Allow-Origin", Origin);
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods","*");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers",
                "Origin, X-Requested-With, Content-Type, Accept, Authorization");
        response.setStatus(401);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        String str = objectMapper.writeValueAsString(errorLoginDto);
        log.info("translate = " + str );
        response.getWriter().write(str);
    }
}
