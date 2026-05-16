/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.port.out;

import java.time.Instant;

public interface TokenRepository {

    String getUserEmail(String token);

    String generateAccessToken(String userEmail);

    String generateRefreshToken(String userEmail);

    Instant getTokenExpirationDate(String token);

    boolean isAccessToken(String token);

    boolean isRefreshToken(String token);
}
