package com.tanji.authapi.dto.oauth;
import com.tanji.authapi.exception.AuthCustomException;
import com.tanji.domainrds.domains.member.domain.Member;
import com.tanji.domainrds.domains.member.domain.Role;

import java.util.Map;

import static com.tanji.authapi.exception.AuthErrorCode.ILLEGAL_REGISTRATION_ID;

public record OAuth2Attributes(
        Map<String, Object> attributes,
        String nameAttributeKey,
        String nickname,
        String email,
        String registerType,
        String socialId
) {
    /**
     * OAuth2 로그인 제공자에 따라 적절한 `OAuth2Attributes` 인스턴스를 생성
     * @param socialName 소셜 로그인 제공자 이름 (예: "kakao", "google).
     * @param userNameAttributeName 사용자 이름 속성의 키.
     * @param attributes OAuth2 리소스 서버에서 받은 사용자 정보.
     */
    public static OAuth2Attributes of(
            String socialName,
            String userNameAttributeName,
            Map<String, Object> attributes
    ) {
        if ("kakao".equals(socialName)) {
            return ofKakao(userNameAttributeName, attributes);
        } else if ("google".equals(socialName)) {
            return ofGoogle(userNameAttributeName, attributes);
        }
        throw new AuthCustomException(ILLEGAL_REGISTRATION_ID);
    }

    private static OAuth2Attributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        return new OAuth2Attributes(
                attributes,
                userNameAttributeName,
                (String) profile.get("nickname"),
                (String) account.get("email"),
                "KAKAO",
                attributes.get("id").toString()
        );
    }

    private static OAuth2Attributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return new OAuth2Attributes(
                attributes,
                userNameAttributeName,
                (String) attributes.get("name"),
                (String) attributes.get("email"),
                "GOOGLE",
                attributes.get("sub").toString()
        );
    }


    /**
     * `OAuth2Attributes`를 `Member` 엔티티로 변환
     */
    public Member toEntity() {
        return Member.builder()
                .provider(registerType)
                .socialId(socialId.toString())
                .nickname(nickname)
                .email(email)
                .role(Role.USER)
                .build();
    }
}

