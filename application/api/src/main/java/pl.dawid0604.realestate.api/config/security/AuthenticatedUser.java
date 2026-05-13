package pl.dawid0604.realestate.api.config.security;

import jakarta.annotation.Nonnull;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import pl.dawid0604.realestate.domain.UserRole;

import java.util.Collection;
import java.util.List;

public record AuthenticatedUser(String email, UserRole role) implements UserDetails {

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
}
