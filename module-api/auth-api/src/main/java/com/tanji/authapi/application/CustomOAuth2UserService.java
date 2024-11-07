package com.tanji.authapi.application;

import com.tanji.authapi.dto.oauth.OAuth2Attributes;
import com.tanji.authapi.dto.oauth.OAuth2UserImpl;
import com.tanji.domainrds.domains.member.domain.Member;
import com.tanji.domainrds.domains.member.service.MemberCommandService;
import com.tanji.domainrds.domains.member.service.MemberQueryService;
import com.tanji.domainredis.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberCommandService memberCommandService;
    private final MemberQueryService memberQueryService;
    private final RedisUtil redisUtil;

    /**
     * OAuth2UserRequest는 인증 과정에서 사용자 정보를 요청할 때 사용하는 객체.
     * ClientRegistration(인증 제공자의 정보) 및 OAuth2AccessToken 포함
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        Map<String, Object> attributes = super.loadUser(userRequest).getAttributes();

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // Provider
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        OAuth2Attributes oauth2Attributes = OAuth2Attributes.of(registrationId, userNameAttributeName, attributes);
        Member member = getOrSave(oauth2Attributes);

        return new OAuth2UserImpl(
                Collections.singleton(new SimpleGrantedAuthority(member.getRole().getKey())),
                attributes,
                oauth2Attributes.nameAttributeKey(),
                member
        );
    }

    private Member getOrSave(OAuth2Attributes oauth2Attributes) {
        // 먼저 회원 조회
        return memberQueryService.findBySocialId(String.valueOf(oauth2Attributes.socialId()))
                .orElseGet(() -> {
                    // 회원이 없는 경우에만 저장
                    Member newMember = oauth2Attributes.toEntity();
                    Member savedMember = memberCommandService.saveMember(newMember); // 먼저 저장하여 ID 생성

                    // 목마름과 배고픔 Redis에 초기화
                    String key = "member:" + newMember.getId() + ":status";
                    Map<String, Integer> statusMap = new HashMap<>(Map.of("thirsty", 100, "hunger", 100));
                    redisUtil.saveAsPermanentValue(key, statusMap);
                    return savedMember;
                });
    }

}
