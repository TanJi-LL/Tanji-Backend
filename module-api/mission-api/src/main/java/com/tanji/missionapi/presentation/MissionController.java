package com.tanji.missionapi.presentation;


import com.tanji.authapi.utils.MemberUtil;
import com.tanji.commonmodule.response.ApiResponse;
import com.tanji.missionapi.application.MissionService;
import com.tanji.missionapi.dto.response.TodayMissionStatusesResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static com.tanji.missionapi.response.MissionSuccessStatus.GET_TODAY_MISSION_SUCCESS;

@Tag(name = "미션 관련 API", description = "오늘의 미션 달성 관리 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/missions")
public class MissionController {

    private final MissionService missionService;

    @Operation(summary = "오늘의 미션 조회", description = "오늘의 미션과 달성 여부를 조회합니다.")
    @GetMapping("/today")
    public ResponseEntity<ApiResponse<TodayMissionStatusesResponse>> getTodayMissionStatuses(Principal principal) {
        Long memberId = MemberUtil.getMemberId(principal);

        return ApiResponse.success(GET_TODAY_MISSION_SUCCESS,
                missionService.getTodayMissionStatuses(memberId));
    }
}
