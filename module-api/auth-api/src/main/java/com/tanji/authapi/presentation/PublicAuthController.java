package com.tanji.authapi.presentation;

import com.tanji.authapi.application.AuthService;
import com.tanji.authapi.dto.request.ReissueTokenRequest;
import com.tanji.authapi.dto.response.JwtResponse;
import com.tanji.commonmodule.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.tanji.authapi.response.AuthSuccessStatus.REISSUE_TOKEN_SUCCESS;

@Tag(name = "인증 인가 API", description = "인증 인가 처리 관련 API")
@RestController
@RequestMapping("/api/v1/public/auth")
@RequiredArgsConstructor
public class PublicAuthController {

    private final AuthService authService;

    @Operation(summary = "엑세스 토큰 재발급 API", description = "리프레쉬 토큰 조회 후 인가용 엑세스 토큰 재발급하는 API")
    @PostMapping
    public ResponseEntity<ApiResponse<JwtResponse>> reissueToken(@Valid @RequestBody ReissueTokenRequest reissueTokenRequest) {
        return ApiResponse.success(REISSUE_TOKEN_SUCCESS, authService.reissueToken(reissueTokenRequest));
    }

}
