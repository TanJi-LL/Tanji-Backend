package com.tanji.authapi.member.controller;

import com.tanji.authapi.member.service.MemberService;
import com.tanji.commonmodule.response.ApiResponse;
import com.tanji.domainrds.domains.member.dto.response.MemberInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static com.tanji.commonmodule.response.SuccessStatus.GET_MEMBER_SUCCESS;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/test")
    public String test(Principal principal) {
        String name = principal.getName();
        System.out.println(name);

//        memberCommandService.createMember(member); // MemberService를 통해 회원 생성
        return "hi";
    }

    @GetMapping
    public ResponseEntity<ApiResponse<MemberInfoResponse>> getMemberInfo(Principal principal) {
        Long memberId = Long.valueOf(principal.getName());
        return ApiResponse.success(GET_MEMBER_SUCCESS,memberService.getMemberInfo(memberId));
    }
}
