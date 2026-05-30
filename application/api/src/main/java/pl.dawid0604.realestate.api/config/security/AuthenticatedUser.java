/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.api.config.security;

import jakarta.annotation.Nonnull;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import pl.dawid0604.realestate.domain.UserRole;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public final class AuthenticatedUser implements UserDetails {
    private final String email;
    private final UserRole role;

    public AuthenticatedUser(final String email, final UserRole role) {
        this.email = email;
        this.role = role;
    }

    @Nonnull
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public @Nullable String getPassword() {
        return null;
    }

    @Nonnull
    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof final AuthenticatedUser other
                && Objects.equals(other.email, this.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
