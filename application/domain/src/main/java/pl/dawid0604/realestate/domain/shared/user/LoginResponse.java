package pl.dawid0604.realestate.domain.shared.user;

import static org.apache.commons.lang3.StringUtils.isBlank;

// TODO: test it
public record LoginResponse(String accessToken, String refreshToken) {

    public LoginResponse {
        if (isBlank(accessToken)) {
            throw new IllegalArgumentException("Access token cannot be blank");
        }

        if (isBlank(refreshToken)) {
            throw new IllegalArgumentException("Refresh token cannot be blank");
        }
    }
}
