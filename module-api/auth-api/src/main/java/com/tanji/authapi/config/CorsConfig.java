package com.tanji.authapi.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CorsConfig implements WebMvcConfigurer {

    public static CorsConfigurationSource apiConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        ArrayList<String> allowedOriginPatterns = new ArrayList<>();
        allowedOriginPatterns.add("http://localhost:8080");
        allowedOriginPatterns.add("https://localhost:8080");

        allowedOriginPatterns.add("http://localhost:3000");
        allowedOriginPatterns.add("https://localhost:3000");

        allowedOriginPatterns.add("https://drinkguide.store");
        allowedOriginPatterns.add("http://drinkguide.store");

        allowedOriginPatterns.add("chrome-extension://koknhbeobbggpdoejlnnenmdkoogpcfp");
        allowedOriginPatterns.add("chrome-extension://cpleniffdlfncjgdgdjdfepkjbjhfbnn");
        allowedOriginPatterns.add("chrome-extension://lcffcfdpmjmmdnmboddohfiljnbbjakh");
        allowedOriginPatterns.add("chrome-extension://ocdkbkdelgibiagdekkgcnpgcnbilbik");
        allowedOriginPatterns.add("chrome-extension://jloifcggdhbkddgfabkjoaknbpffpppo");

        configuration.setAllowedOrigins(allowedOriginPatterns);
        configuration.setAllowedMethods(List.of("HEAD", "POST", "GET", "DELETE", "PUT", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(List.of(HttpHeaders.LOCATION, HttpHeaders.SET_COOKIE));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
