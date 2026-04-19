/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.port.out;

public interface PasswordEncoder {

    String encode(String plainPassword);

    boolean matches(String plainPassword, String encodedPassword);
}
