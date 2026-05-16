package pl.dawid0604.realestate.api.auth.request;

import pl.dawid0604.realestate.api.validation.ValidRefreshToken;

public record RefreshTokenRequest(@ValidRefreshToken String refreshToken) {}
