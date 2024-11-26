package com.tanji.memberapi.presentation;

import com.tanji.authapi.utils.MemberUtil;
import com.tanji.commonmodule.response.ApiResponse;
import com.tanji.memberapi.application.MemberService;
import com.tanji.memberapi.dto.GetImpactSummaryResponse;
import com.tanji.memberapi.response.TanjiStatusSuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Tag(name = "회원 관련 API", description = "회원 정보 관련 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;
    /**
     * 테스트용
     */
//    @GetMapping
//    public ResponseEntity<Member> getMemberById(@RequestParam("memberId") Long memberId, Authentication authentication) {
//        log.info("Path Variable ID: {}", memberId);
//        log.info("Authentication Get Name: {}", authentication.getName());
//        Member member = memberService.findById(memberId);
//        return ResponseEntity.ok(member);
//    }

    @Operation(summary = "회원의 탄소 절약량 및 랭킹 조회", description = "회원의 탄소 절약량 및 랭킹을 조회합니다.")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<GetImpactSummaryResponse>> getMemberImpactSummary(Principal principal) {
        Long memberId = MemberUtil.getMemberId(principal);
        return ApiResponse.success(TanjiStatusSuccessStatus.GET_IMPACT_SUMMARY_SUCCESS, memberService.getMemberImpactSummary(memberId));
    }
}
