package com.tanji.memberapi.presentation;

import com.tanji.domainrds.domains.member.domain.Member;
import com.tanji.memberapi.application.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @GetMapping("/{memberId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')") // JwtAccessDeniedHandler 작동함
    public ResponseEntity<Member> getMemberById(@PathVariable("memberId") Long memberId, Principal principal) {
        Member member = memberService.findById(memberId);
        return ResponseEntity.ok(member);
    }

//    @GetMapping("/{memberId}")
//    @PreAuthorize("#memberId == authentication.name") // 또는 .equals() 사용 가능
//    public String getMemberById(@PathVariable("memberId") String memberId, Authentication authentication) {
//        log.info("Path Variable ID: {}", memberId);
//        log.info("Authentication Get Name: {}", authentication.getName());
//
//        return "hi";
//    }

}
