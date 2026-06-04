/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Token response")
public record TokenResponseDto(
        @Schema(description = "Access token", example = "eyJhbGciOiJIUzI1NiJ9...")
                String accessToken,
        @Schema(description = "Refresh token", example = "eyJhbGciOiJIUzI1NiJ9...")
                String refreshToken) {}
