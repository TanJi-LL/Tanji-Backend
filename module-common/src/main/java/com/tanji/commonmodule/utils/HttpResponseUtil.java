package com.tanji.commonmodule.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tanji.commonmodule.response.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Component
public class HttpResponseUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void setSuccessResponse(HttpServletResponse response, HttpStatus httpStatus, Object body) throws IOException {
        log.info("[*] Success Response");
        ApiResponse<Object> apiResponse = ApiResponse.<Object>builder()
                .status(httpStatus.value())
                .success(true)
                .message(httpStatus.getReasonPhrase())
                .data(body)
                .build();
        String responseBody = objectMapper.writeValueAsString(apiResponse);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(httpStatus.value());
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(responseBody);
    }


    public static void setErrorResponse(HttpServletResponse response, HttpStatus httpStatus, ResponseEntity responseEntity) throws IOException {
        log.info("[*] Failure Response");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(httpStatus.value());
        response.setCharacterEncoding("UTF-8");
        response.getWriter().println(objectMapper.writeValueAsString(responseEntity.getBody()));
    }
}