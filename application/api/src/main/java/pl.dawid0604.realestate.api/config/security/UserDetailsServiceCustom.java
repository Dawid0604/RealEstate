package pl.dawid0604.realestate.api.config.security;

import static lombok.AccessLevel.PACKAGE;

import jakarta.annotation.Nonnull;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import pl.dawid0604.realestate.domain.port.out.UserRepository;

@Service
@RequiredArgsConstructor(access = PACKAGE)
class UserDetailsServiceCustom implements UserDetailsService {
    private final UserRepository userRepository;

    @Nonnull
    @Override
    public UserDetails loadUserByUsername(@Nonnull final String username)
            throws UsernameNotFoundException {

        return userRepository
                .findUserRoleByEmail(username)
                .map(role -> new AuthenticatedUser(username, role))
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
