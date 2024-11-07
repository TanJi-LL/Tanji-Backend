package com.juseungl.statusapi.presentation;

import com.juseungl.statusapi.application.TanjiStatusService;
import com.tanji.authapi.utils.MemberUtil;
import com.tanji.commonmodule.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/status")
public class TanjiStatusController {
    private final TanjiStatusService tanjiStatusService;

    @GetMapping
    public ResponseEntity<ApiResponse<Object>> getMemberById(Principal principal) {
        Long memberId = MemberUtil.getMemberId(principal);
        return ApiResponse.success(tanjiStatusService.);
    }
}
