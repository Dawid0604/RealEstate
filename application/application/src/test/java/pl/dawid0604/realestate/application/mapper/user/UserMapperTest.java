/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.mapper.user;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import pl.dawid0604.realestate.application.dto.user.UserProfileDto;
import pl.dawid0604.realestate.application.fixture.UserFixture;
import pl.dawid0604.realestate.domain.UserRole;
import pl.dawid0604.realestate.domain.UserStatus;
import pl.dawid0604.realestate.domain.UserType;
import pl.dawid0604.realestate.domain.shared.user.projection.UserProfileProjection;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = Mappers.getMapper(UserMapper.class);
    }

    @Test
    @DisplayName("Should map to UserProfileDto properly")
    void shouldMapToUserProfileDtoProperly() {
        // Given
        final UserProfileProjection projection = mock(UserProfileProjection.class);

        // When
        given(projection.getUserId()).willReturn(UUID.randomUUID());
        given(projection.getEmail()).willReturn(UserFixture.getDummyEmail());
        given(projection.getFirstName()).willReturn("John");
        given(projection.getLastName()).willReturn("Doe");
        given(projection.getContactPhoneNumber()).willReturn("123456789");
        given(projection.getContactEmail()).willReturn("anyemail@mail.com");
        given(projection.getAvatarUrl()).willReturn("https://xyz");
        given(projection.getRole()).willReturn(UserRole.ADMIN_ROLE.name());
        given(projection.getType()).willReturn(UserType.AGENCY.name());
        given(projection.getStatus()).willReturn(UserStatus.ACTIVE.name());

        final UserProfileDto result = userMapper.toUserProfileDto(projection);

        // Then
        Assertions.assertThat(result)
                .returns(projection.getUserId(), UserProfileDto::userId)
                .returns(projection.getEmail(), UserProfileDto::email)
                .returns(projection.getContactPhoneNumber(), UserProfileDto::contactPhoneNumber)
                .returns(projection.getContactEmail(), UserProfileDto::contactEmail)
                .returns(projection.getRole(), UserProfileDto::role)
                .returns(projection.getType(), UserProfileDto::type)
                .returns(projection.getStatus(), UserProfileDto::status)
                .returns(
                        projection.getFirstName() + " " + projection.getLastName(),
                        UserProfileDto::fullName)
                .returns(projection.getAvatarUrl(), UserProfileDto::avatarUrl);
    }

    @Nested
    final class ToFullNameTests {

        @Test
        @DisplayName("Should return null when projection is null")
        void shouldReturnNullWhenProjectionIsNull() {
            // Given
            // When
            final String result = userMapper.toFullName(null);

            // Then
            Assertions.assertThat(result).isNull();
        }

        @ParameterizedTest
        @CsvSource({
            "John,Doe,John Doe",
            "John,,John",
            ",Doe,Doe",
            ",,",
            " John , Doe ,John Doe",
        })
        @DisplayName("Should combine first name and last name")
        void shouldCombineFirstNameAndLastName(
                final String firstName, final String lastName, final String expectedFullName) {

            // Given
            final UserProfileProjection projection = mock(UserProfileProjection.class);

            given(projection.getFirstName()).willReturn(firstName);
            given(projection.getLastName()).willReturn(lastName);

            // When
            final String result = userMapper.toFullName(projection);

            // Then
            Assertions.assertThat(result).isEqualTo(expectedFullName);
        }
    }
}
