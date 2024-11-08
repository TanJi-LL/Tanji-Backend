package com.tanji.statusapi.presentation;

import com.tanji.statusapi.application.TanjiStatusService;
import com.tanji.statusapi.dto.response.GetTanjiStatusResponse;
import com.tanji.authapi.utils.MemberUtil;
import com.tanji.commonmodule.response.ApiResponse;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static com.tanji.statusapi.response.TanjiStatusSuccessStatus.FEED_TANJI_SUCCESS;
import static com.tanji.statusapi.response.TanjiStatusSuccessStatus.GET_STATUS_SUCCESS;

@Tag(name = "탄지 상태 관련 API", description = "탄지 배고픔 그리고 목마름 상태 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/status")
public class TanjiStatusController {
    private final TanjiStatusService tanjiStatusService;

    @Operation(summary = "탄지 상태 조회", description = "탄지의 배고픔 및 목마름 상태 그리고 먹이 및 물 수를 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<GetTanjiStatusResponse>> getTanjiStatus(Principal principal) {
        Long memberId = MemberUtil.getMemberId(principal);
        return ApiResponse.success(GET_STATUS_SUCCESS, tanjiStatusService.getTanjiStatus(memberId));
    }

    @Operation(summary = "탄지 먹이 주기", description = "남은 먹이 수를 감소 시키고 배고픔 정도를 증가시킵니다.")
    @PatchMapping("/feed")
    public ResponseEntity<ApiResponse<GetTanjiStatusResponse>> feedTanji(Principal principal) {
        Long memberId = MemberUtil.getMemberId(principal);
        return ApiResponse.success(FEED_TANJI_SUCCESS,tanjiStatusService.feedTanji(memberId));
    }
}
