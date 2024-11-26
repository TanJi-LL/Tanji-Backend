package com.tanji.missionapi.presentation;


import com.tanji.authapi.utils.MemberUtil;
import com.tanji.commonmodule.response.ApiResponse;
import com.tanji.missionapi.application.MissionService;
import com.tanji.missionapi.dto.response.CompleteMissionResponse;
import com.tanji.missionapi.dto.response.GetTodayMissionStatusesResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static com.tanji.missionapi.response.MissionSuccessStatus.GET_TODAY_MISSION_SUCCESS;
import static com.tanji.missionapi.response.MissionSuccessStatus.UPDATE_MISSION_SUCCESS;

@Tag(name = "미션 관련 API", description = "오늘의 미션 달성 상태 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/missions")
public class MissionController {

    private final MissionService missionService;

    @Operation(summary = "오늘의 미션 및 달성 상태 조회", description = "오늘의 미션과 달성 상태를 조회합니다.")
    @GetMapping("/today")
    public ResponseEntity<ApiResponse<GetTodayMissionStatusesResponse>> getTodayMissionStatuses(Principal principal) {
        Long memberId = MemberUtil.getMemberId(principal);

        return ApiResponse.success(GET_TODAY_MISSION_SUCCESS,
                missionService.getTodayMissionStatuses(memberId));
    }

    @Operation(summary = "오늘의 미션 달성 상태 업데이트", description = "오늘의 미션을 달성 상태를 업데이트하고 먹이 수를 증가시킵니다.")
    @PutMapping("/today/{missionId}")
    public ResponseEntity<ApiResponse<CompleteMissionResponse>> completeMission(
            @PathVariable int missionId,
            Principal principal) {
        Long memberId = MemberUtil.getMemberId(principal);

        return ApiResponse.success(UPDATE_MISSION_SUCCESS,
                missionService.completeMission(memberId, missionId));
    }
}
