package com.tanji.authapi.application;

import com.tanji.authapi.dto.oauth.OAuth2UserImpl;
import com.tanji.domainrds.domains.member.domain.Member;
import com.tanji.domainrds.domains.member.service.MemberQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberQueryService memberQueryService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 여기서 username은 id value
        Member member = memberQueryService.findById(Long.valueOf(username))
                .orElseThrow(() -> new UsernameNotFoundException("id가 " + username + "인 유저는 존재하지 않습니다."));

        
        return new OAuth2UserImpl(
                Collections.singleton(new SimpleGrantedAuthority(member.getRole().name())),
                createAttributes(member),
                "id",
                member
        );
    }

    private Map<String, Object> createAttributes(Member member) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("id", member.getId());
        attributes.put("provider", member.getProvider());
        attributes.put("socialId", member.getSocialId());
        attributes.put("nickname", member.getNickname());
        attributes.put("email", member.getEmail());
        attributes.put("role", member.getRole());
        return attributes;
    }

}
