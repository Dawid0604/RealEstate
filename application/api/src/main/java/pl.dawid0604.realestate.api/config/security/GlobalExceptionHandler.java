package pl.dawid0604.realestate.api.config.security;

import jakarta.validation.ConstraintViolationException;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import pl.dawid0604.realestate.domain.shared.exception.AdvertisementNotFoundException;
import pl.dawid0604.realestate.domain.shared.exception.DifferentPasswordException;
import pl.dawid0604.realestate.domain.shared.exception.ForbiddenException;
import pl.dawid0604.realestate.domain.shared.exception.InternalException;
import pl.dawid0604.realestate.domain.shared.exception.InvalidArgumentValueException;
import pl.dawid0604.realestate.domain.shared.exception.InvalidCredentialsException;
import pl.dawid0604.realestate.domain.shared.exception.LocalityExistsException;
import pl.dawid0604.realestate.domain.shared.exception.LocalityNotFoundException;
import pl.dawid0604.realestate.domain.shared.exception.MaxPhotosExceededException;
import pl.dawid0604.realestate.domain.shared.exception.RefreshTokenNotFoundException;
import pl.dawid0604.realestate.domain.shared.exception.UserAlreadyActiveException;
import pl.dawid0604.realestate.domain.shared.exception.UserBannedException;
import pl.dawid0604.realestate.domain.shared.exception.UserCannotBeActivatedException;
import pl.dawid0604.realestate.domain.shared.exception.UserCannotBeUnbannedException;
import pl.dawid0604.realestate.domain.shared.exception.UserExistsException;
import pl.dawid0604.realestate.domain.shared.exception.UserNotFoundException;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
class GlobalExceptionHandler {
    private static final String PROBLEM_DETAILS_TYPE_URI_PREFIX = "https://realestate.pl/errors/";

    @ExceptionHandler(AdvertisementNotFoundException.class)
    ProblemDetail handleAdvertisementNotFoundException(final AdvertisementNotFoundException ex) {
        return toProblemDetail(HttpStatus.NOT_FOUND, ex.getMessage(), "advertisement-not-found");
    }

    @ExceptionHandler(UserNotFoundException.class)
    ProblemDetail handleUserNotFoundException(final UserNotFoundException ex) {
        return toProblemDetail(HttpStatus.NOT_FOUND, ex.getMessage(), "user-not-found");
    }

    @ExceptionHandler(LocalityNotFoundException.class)
    ProblemDetail handleLocalityNotFoundException(final LocalityNotFoundException ex) {
        return toProblemDetail(HttpStatus.NOT_FOUND, ex.getMessage(), "locality-not-found");
    }

    @ExceptionHandler(RefreshTokenNotFoundException.class)
    ProblemDetail handleRefreshTokenNotFoundException(final RefreshTokenNotFoundException ex) {
        return toProblemDetail(HttpStatus.NOT_FOUND, ex.getMessage(), "refresh-token-not-found");
    }

    @ExceptionHandler(DifferentPasswordException.class)
    ProblemDetail handleDifferentPasswordException(final DifferentPasswordException ex) {
        return toProblemDetail(HttpStatus.CONFLICT, ex.getMessage(), "invalid-password");
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    ProblemDetail handleInvalidCredentialsException() {
        return toProblemDetail(
                HttpStatus.UNAUTHORIZED, "Invalid credentials", "invalid-credentials");
    }

    @ExceptionHandler(ForbiddenException.class)
    ProblemDetail handleForbiddenException(final ForbiddenException ex) {
        return toProblemDetail(HttpStatus.FORBIDDEN, ex.getMessage(), "forbidden");
    }

    @ExceptionHandler(InvalidArgumentValueException.class)
    ProblemDetail handleInvalidArgumentValueException(
            final InvalidArgumentValueException exception) {
        return toProblemDetail(
                HttpStatus.UNPROCESSABLE_CONTENT, exception.getMessage(), "unprocessable-content");
    }

    @ExceptionHandler(MaxPhotosExceededException.class)
    ProblemDetail handleMaxPhotosExceededException(final MaxPhotosExceededException exception) {
        return toProblemDetail(
                HttpStatus.UNPROCESSABLE_CONTENT, exception.getMessage(), "limit-exceeded");
    }

    @ExceptionHandler(UserAlreadyActiveException.class)
    ProblemDetail handleAlreadyActiveException(final UserAlreadyActiveException ex) {
        return toProblemDetail(HttpStatus.CONFLICT, ex.getMessage(), "user-already-active");
    }

    @ExceptionHandler(UserCannotBeActivatedException.class)
    ProblemDetail handleUserCannotBeActivatedException(final UserCannotBeActivatedException ex) {
        return toProblemDetail(HttpStatus.CONFLICT, ex.getMessage(), "user-cannot-be-activated");
    }

    @ExceptionHandler(UserCannotBeUnbannedException.class)
    ProblemDetail handleUserCannotBeUnbannedException(final UserCannotBeUnbannedException ex) {
        return toProblemDetail(HttpStatus.CONFLICT, ex.getMessage(), "user-cannot-be-unbanned");
    }

    @ExceptionHandler(UserBannedException.class)
    ProblemDetail handleUserBannedException(final UserBannedException ex) {
        return toProblemDetail(HttpStatus.FORBIDDEN, ex.getMessage(), "user-banned");
    }

    @ExceptionHandler(UserExistsException.class)
    ProblemDetail handleUserExistsException(final UserExistsException ex) {
        return toProblemDetail(HttpStatus.CONFLICT, ex.getMessage(), "user-exists");
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ProblemDetail handleAccessDenied() {
        return toProblemDetail(HttpStatus.FORBIDDEN, "You have no permissions", "access-denied");
    }

    @ExceptionHandler(LocalityExistsException.class)
    ProblemDetail handleLocalityExistsException(final LocalityExistsException ex) {
        return toProblemDetail(HttpStatus.CONFLICT, ex.getMessage(), "locality-exists");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    ProblemDetail handleDataIntegrityViolationException(final DataIntegrityViolationException ex) {
        log.error("Unexpected error", ex);
        return toProblemDetail(
                HttpStatus.CONFLICT, "Resource already exists", "data-integrity-violation");
    }

    @ExceptionHandler({Exception.class, InternalException.class})
    ProblemDetail handleUnexpected(final Exception ex) {
        log.error("Unexpected error", ex);
        return toProblemDetail(
                HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", "internal-error");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail handleValidationErrors(final MethodArgumentNotValidException ex) {
        final ProblemDetail problemDetail =
                toProblemDetail(HttpStatus.BAD_REQUEST, "Validation failed", "validation-error");

        problemDetail.setProperty("errors", getValidationErrors(ex));
        return problemDetail;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ProblemDetail handleConstraintViolationException(final ConstraintViolationException ex) {
        final ProblemDetail problemDetail =
                toProblemDetail(
                        HttpStatus.BAD_REQUEST, "Constraint violation", "constraint-violation");

        problemDetail.setProperty("errors", getConstraintViolationErrors(ex));
        return problemDetail;
    }

    public static ProblemDetail toProblemDetail(
            final HttpStatus status, final String message, final String errorCode) {

        final ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, message);
        problemDetail.setType(getProblemType(errorCode));
        problemDetail.setProperty("errorCode", errorCode);
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    private static URI getProblemType(final String errorCode) {
        return URI.create(PROBLEM_DETAILS_TYPE_URI_PREFIX + errorCode);
    }

    private static List<Map<String, String>> getValidationErrors(
            final MethodArgumentNotValidException ex) {

        return ex.getBindingResult().getFieldErrors().stream()
                .map(
                        e ->
                                Map.of(
                                        "field", e.getField(),
                                        "message",
                                                Objects.requireNonNullElse(
                                                        e.getDefaultMessage(), StringUtils.EMPTY),
                                        "rejectedValue",
                                                Objects.requireNonNullElse(
                                                        (String) e.getRejectedValue(),
                                                        StringUtils.EMPTY)))
                .toList();
    }

    private static List<Map<String, String>> getConstraintViolationErrors(
            final ConstraintViolationException ex) {

        return ex.getConstraintViolations().stream()
                .map(
                        e ->
                                Map.of(
                                        "field", e.getPropertyPath().toString(),
                                        "message", e.getMessage()))
                .toList();
    }
}
