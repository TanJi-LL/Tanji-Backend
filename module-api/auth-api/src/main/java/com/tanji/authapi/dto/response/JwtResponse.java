package com.tanji.authapi.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public record JwtResponse(
        @Schema(description = "엑세스 토큰") @JsonProperty("accessToken") String accessToken,
        @Schema(description = "리프레쉬 토큰") @JsonProperty("refreshToken") String refreshToken
) {}