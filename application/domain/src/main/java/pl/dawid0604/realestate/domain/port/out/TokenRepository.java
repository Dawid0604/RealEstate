/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.port.out;

public interface TokenRepository {

    String getUserEmail(String token);

    String generateAccessToken(String userEmail);

    String generateRefreshToken(String userEmail);
}
