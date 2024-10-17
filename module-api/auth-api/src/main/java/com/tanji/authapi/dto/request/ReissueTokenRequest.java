package com.tanji.authapi.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ReissueTokenRequest(
        @NotBlank(message = "Refresh token은 비어 있을 수 없습니다.")
        String refreshToken
) {
}
