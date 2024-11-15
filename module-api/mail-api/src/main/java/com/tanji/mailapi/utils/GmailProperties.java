package com.tanji.mailapi.utils;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter @Setter
@ConfigurationProperties("google")
public class GmailProperties {
    private String clientId;
    private String clientSecret;
    public static final String APPLICATION_NAME = "TanjiGmail";
}
