/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.command.handler.advertisement;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import static pl.dawid0604.realestate.application.fixture.AdvertisementFixture.getDummyAdvertisementBuilder;
import static pl.dawid0604.realestate.application.fixture.AdvertisementFixture.getDummyCommercialDetails;
import static pl.dawid0604.realestate.application.fixture.AdvertisementFixture.getDummyFlatDetails;
import static pl.dawid0604.realestate.application.fixture.AdvertisementFixture.getDummyHouseDetails;
import static pl.dawid0604.realestate.application.fixture.AdvertisementFixture.getDummyPlotDetails;
import static pl.dawid0604.realestate.application.fixture.UserFixture.getDummyEmail;
import static pl.dawid0604.realestate.application.fixture.UserFixture.getDummyUserBuilder;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pl.dawid0604.realestate.domain.Advertisement;
import pl.dawid0604.realestate.domain.AdvertisementDetails;
import pl.dawid0604.realestate.domain.Title;
import pl.dawid0604.realestate.domain.User;
import pl.dawid0604.realestate.domain.UserStatus;
import pl.dawid0604.realestate.domain.port.out.AdvertisementRepository;
import pl.dawid0604.realestate.domain.port.out.UserRepository;
import pl.dawid0604.realestate.domain.shared.AdvertisementType;
import pl.dawid0604.realestate.domain.shared.exception.AdvertisementNotFoundException;
import pl.dawid0604.realestate.domain.shared.exception.UnauthorizedAccessException;
import pl.dawid0604.realestate.domain.shared.exception.UserNotFoundException;

import java.util.Optional;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
class UpdateAdvertisementTitleHandlerTest {
    @Mock private AdvertisementRepository advertisementRepository;
    @Mock private UserRepository userRepository;
    @Captor private ArgumentCaptor<Advertisement> advertisementArgumentCaptor;
    @Captor private ArgumentCaptor<Title> titleArgumentCaptor;
    private UpdateAdvertisementTitleHandler handler;

    @BeforeEach
    void setUp() {
        handler = new UpdateAdvertisementTitleHandler(advertisementRepository, userRepository);
    }

    @Test
    @DisplayName("Should throw exception when user not found")
    void shouldThrowExceptionWhenUserNotFound() {
        // Given
        final UpdateAdvertisementTitleCommand command = getCommand();

        // When
        // Then
        Assertions.assertThatThrownBy(() -> handler.handle(command))
                .isExactlyInstanceOf(UserNotFoundException.class);

        verify(userRepository, never()).save(any());
        verify(advertisementRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when user is inactive")
    void shouldThrowExceptionWhenUserIsInactive() {
        // Given
        final UpdateAdvertisementTitleCommand command = getCommand();
        final User foundUser = getDummyUserBuilder().status(UserStatus.INACTIVE).build();

        given(userRepository.findByEmail(command.userEmail())).willReturn(Optional.of(foundUser));

        // When
        // Then
        Assertions.assertThatThrownBy(() -> handler.handle(command))
                .isExactlyInstanceOf(UnauthorizedAccessException.class);

        verify(userRepository, never()).save(any());
        verify(advertisementRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when advertisement not found")
    void shouldThrowExceptionWhenAdvertisementNotFound() {
        // Given
        final UpdateAdvertisementTitleCommand command = getCommand();
        final User foundUser = getDummyUserBuilder().build();

        given(userRepository.findByEmail(command.userEmail())).willReturn(Optional.of(foundUser));

        // When
        // Then
        Assertions.assertThatThrownBy(() -> handler.handle(command))
                .isExactlyInstanceOf(AdvertisementNotFoundException.class);

        verify(userRepository, never()).save(any());
        verify(advertisementRepository, never()).save(any());
    }

    @ParameterizedTest
    @MethodSource("detailsDataProvider")
    @DisplayName("Should update title")
    void shouldUpdateTitle(final AdvertisementDetails<?> details) {
        // Given
        final UpdateAdvertisementTitleCommand command = getCommand();
        final User foundUser = getDummyUserBuilder().build();
        final Advertisement foundAdvertisement =
                spy(getDummyAdvertisementBuilder(details).userId(foundUser.getId()).build());

        given(userRepository.findByEmail(command.userEmail())).willReturn(Optional.of(foundUser));
        given(
                        advertisementRepository.findBySlug(
                                command.slug(), AdvertisementType.of(command.advertisementType())))
                .willReturn(Optional.of(foundAdvertisement));

        // When
        handler.handle(command);

        // Then
        verify(advertisementRepository).save(advertisementArgumentCaptor.capture());
        verify(foundAdvertisement).updateTitle(titleArgumentCaptor.capture());

        Assertions.assertThat(advertisementArgumentCaptor.getValue()).isEqualTo(foundAdvertisement);
        Assertions.assertThat(titleArgumentCaptor.getValue().value()).isEqualTo(command.newTitle());
    }

    private static Stream<Arguments> detailsDataProvider() {
        return Stream.of(
                Arguments.of(getDummyFlatDetails()),
                Arguments.of(getDummyCommercialDetails()),
                Arguments.of(getDummyHouseDetails()),
                Arguments.of(getDummyPlotDetails()));
    }

    private static UpdateAdvertisementTitleCommand getCommand() {
        return new UpdateAdvertisementTitleCommand(
                "abcde", "new any title content", AdvertisementType.FLAT.name(), getDummyEmail());
    }
}
