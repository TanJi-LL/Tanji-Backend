package com.tanji.memberapi.presentation;

import com.tanji.domainrds.domains.member.domain.Member;
import com.tanji.memberapi.application.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {
    private final MemberService memberService;

    /**
     * 테스트용
     */
    @GetMapping("/{memberId}")
    public ResponseEntity<Member> getMemberById(@PathVariable("memberId") Long memberId) {
        log.info("Fetching member with ID: {}", memberId);
        Member member = memberService.findById(memberId);
        return ResponseEntity.ok(member);
    }
}
