/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.password;

import static lombok.AccessLevel.PACKAGE;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pl.dawid0604.realestate.domain.port.out.PasswordRepository;

@Service
@RequiredArgsConstructor(access = PACKAGE)
class PasswordAdapter implements PasswordRepository {
    private final PasswordEncoder passwordEncoder;

    @Override
    public String encode(final String plainPassword) {
        return passwordEncoder.encode(plainPassword);
    }

    @Override
    public boolean matches(final String plainPassword, final String encodedPassword) {
        return passwordEncoder.matches(plainPassword, encodedPassword);
    }
}
