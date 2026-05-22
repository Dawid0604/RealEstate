/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.api.config.security;

import static lombok.AccessLevel.PACKAGE;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor(access = PACKAGE)
class AccessDeniedHandlerCustom implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;

    @Override
    public void handle(
            @Nonnull final HttpServletRequest request,
            @Nonnull final HttpServletResponse response,
            @Nonnull final AccessDeniedException accessDeniedException)
            throws IOException {

        final ProblemDetail problemDetail =
                GlobalExceptionHandler.toProblemDetail(
                        HttpStatus.FORBIDDEN, "Access denied", "access-denied");

        response.setStatus(problemDetail.getStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(problemDetail));
    }
}
