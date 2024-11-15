package com.tanji.mailapi.application;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.tanji.authapi.exception.AuthCustomException;
import com.tanji.mailapi.utils.GmailProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import com.google.api.client.http.HttpTransport;

import static com.tanji.authapi.exception.AuthErrorCode.UNAUTHORIZED_MEMBER;
import static com.tanji.mailapi.utils.GmailProperties.APPLICATION_NAME;

@Slf4j
@RequiredArgsConstructor
@Service
public class GmailService {

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance(); // Google API 클라이언트에서JSON 데이터의 직렬화 및 역직렬화에 사용하는 인터페이스
    private final OAuth2AuthorizedClientService authorizedClientService;
    private final GmailProperties gmailProperties;

    public Gmail getGmailService(Long memberId) throws GeneralSecurityException, IOException {
        // Gmail 클라이언트 생성
        return new Gmail.Builder(GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY, getGoogleCredential(memberId))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    /**
     * Credential(Google API 인증 자격 증명) 객체 생성
     */
    public Credential getGoogleCredential(Long memberId) throws GeneralSecurityException, IOException {
        // OAuth2 인증 클라이언트 정보 조회
        OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(
                "google",
                String.valueOf(memberId)
        );

        if (authorizedClient == null || authorizedClient.getAccessToken() == null) {
            throw new AuthCustomException(UNAUTHORIZED_MEMBER);
        }

        String accessToken = authorizedClient.getAccessToken().getTokenValue();
        String refreshToken = authorizedClient.getRefreshToken() != null ? authorizedClient.getRefreshToken().getTokenValue() : null;

        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport(); // HTTP 전송 객체 생성

        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(httpTransport)
                .setJsonFactory(JSON_FACTORY)
                .setClientSecrets(gmailProperties.getClientId(),
                        gmailProperties.getClientSecret())
                .build()
                .setAccessToken(accessToken);

        if (refreshToken != null) {
            credential.setRefreshToken(refreshToken);
        }

        return credential;
    }
}