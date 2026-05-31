package pl.dawid0604.realestate.api.config.security;

import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
class MdcUserFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            @Nonnull final HttpServletRequest request,
            @Nonnull final HttpServletResponse response,
            final FilterChain filterChain)
            throws ServletException, IOException {

        try {
            getRequestUsername().ifPresent(username -> MDC.put("username", username));
            MDC.put("requestId", UUID.randomUUID().toString());

            filterChain.doFilter(request, response);

        } finally {
            MDC.clear();
        }
    }

    private static Optional<String> getRequestUsername() {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null
                && auth.isAuthenticated()
                && !(auth instanceof AnonymousAuthenticationToken)) {

            return Optional.ofNullable(auth.getName());
        }

        return Optional.empty();
    }
}
