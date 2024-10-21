package com.tanji.memberapi.presentation;

import com.tanji.domainrds.domains.member.domain.Member;
import com.tanji.memberapi.application.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {
    private final MemberService memberService;
    /**
     * 테스트용
     */
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Member> getMemberById(@RequestParam("memberId") Long memberId, Authentication authentication) {
        log.info("Path Variable ID: {}", memberId);
        log.info("Authentication Get Name: {}", authentication.getName());
        Member member = memberService.findById(memberId);
        return ResponseEntity.ok(member);
    }
}
