/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.query.handler.user;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pl.dawid0604.realestate.application.dto.user.UserProfileDto;
import pl.dawid0604.realestate.application.fixture.UserFixture;
import pl.dawid0604.realestate.application.mapper.user.UserMapper;
import pl.dawid0604.realestate.application.query.UserProfileQuery;
import pl.dawid0604.realestate.domain.port.out.UserRepository;
import pl.dawid0604.realestate.domain.shared.exception.UserNotFoundException;
import pl.dawid0604.realestate.domain.shared.user.projection.UserProfileProjection;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserProfileQueryHandlerTest {
    @Mock private UserRepository userRepository;
    @Mock private UserMapper userMapper;
    private UserProfileQueryHandler handler;

    @BeforeEach
    void setUp() {
        handler = new UserProfileQueryHandler(userRepository, userMapper);
    }

    @Test
    @DisplayName("Should throw exception when query is null")
    void shouldThrowExceptionWhenQueryIsNull() {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(() -> handler.handle(null))
                .isExactlyInstanceOf(NullPointerException.class)
                .hasMessage("Query cannot be null");
    }

    @Test
    @DisplayName("Should throw exception when user not found")
    void shouldThrowExceptionWhenUserNotFound() {
        // Given
        final UserProfileQuery query = getValidQuery();

        // When
        // Then
        Assertions.assertThatThrownBy(() -> handler.handle(query))
                .isExactlyInstanceOf(UserNotFoundException.class);

        verify(userRepository).findUserProfile(query.email());
        verifyNoInteractions(userMapper);
    }

    @Test
    @DisplayName("Should handle successfully")
    void shouldHandleSuccessfully() {
        // Given
        final UserProfileQuery query = getValidQuery();
        final UserProfileProjection projection = mock(UserProfileProjection.class);

        given(userRepository.findUserProfile(query.email())).willReturn(Optional.of(projection));
        given(userMapper.toUserProfileDto(projection)).willReturn(mock(UserProfileDto.class));

        // When
        final UserProfileDto result = handler.handle(query);

        // Then
        Assertions.assertThat(result).isNotNull();
    }

    private static UserProfileQuery getValidQuery() {
        return new UserProfileQuery(UserFixture.getDummyEmail());
    }
}
