/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.query.handler.user;

import static org.mockito.BDDMockito.given;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pl.dawid0604.realestate.application.fixture.UserFixture;
import pl.dawid0604.realestate.application.query.IsUserBannedQuery;
import pl.dawid0604.realestate.domain.UserStatus;
import pl.dawid0604.realestate.domain.port.out.UserRepository;

@ExtendWith(MockitoExtension.class)
class IsUserBannedQueryHandlerTest {
    @Mock private UserRepository userRepository;
    private IsUserBannedQueryHandler handler;

    @BeforeEach
    void setUp() {
        handler = new IsUserBannedQueryHandler(userRepository);
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

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    @DisplayName("Should handle successfully")
    void shouldHandleSuccessfully(final boolean expectedValue) {
        // Given
        final IsUserBannedQuery query = new IsUserBannedQuery(UserFixture.getDummyEmail());

        given(userRepository.hasStatus(query.email(), UserStatus.BANNED)).willReturn(expectedValue);

        // When
        final boolean result = handler.handle(query);

        // Then
        Assertions.assertThat(result).isEqualTo(expectedValue);
    }
}
