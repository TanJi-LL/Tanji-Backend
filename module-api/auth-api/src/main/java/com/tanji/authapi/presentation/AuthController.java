package com.tanji.authapi.presentation;

import com.tanji.authapi.application.AuthService;
import com.tanji.authapi.dto.request.ReissueTokenRequest;
import com.tanji.authapi.dto.response.JwtResponse;
import com.tanji.commonmodule.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.tanji.authapi.response.AuthSuccessStatus.REISSUE_TOKEN_SUCCESS;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping
    public ResponseEntity<ApiResponse<JwtResponse>> reissueToken(@RequestBody ReissueTokenRequest reissueTokenRequest) {
        return ApiResponse.success(REISSUE_TOKEN_SUCCESS, authService.reissueToken(reissueTokenRequest));
    }

}
