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
import org.springframework.context.ApplicationEventPublisher;

import pl.dawid0604.realestate.application.command.SetAsSoldAdvertisementCommand;
import pl.dawid0604.realestate.domain.Advertisement;
import pl.dawid0604.realestate.domain.AdvertisementDetails;
import pl.dawid0604.realestate.domain.User;
import pl.dawid0604.realestate.domain.UserStatus;
import pl.dawid0604.realestate.domain.port.out.AdvertisementRepository;
import pl.dawid0604.realestate.domain.port.out.UserRepository;
import pl.dawid0604.realestate.domain.shared.event.AdvertisementStatusChangedEvent;
import pl.dawid0604.realestate.domain.shared.exception.AdvertisementNotFoundException;
import pl.dawid0604.realestate.domain.shared.exception.UnauthorizedAccessException;
import pl.dawid0604.realestate.domain.shared.exception.UserNotFoundException;

import java.util.Optional;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
class SetAsSoldAdvertisementHandlerTest {
    @Mock private AdvertisementRepository advertisementRepository;
    @Mock private UserRepository userRepository;
    @Mock private ApplicationEventPublisher eventPublisher;
    @Captor private ArgumentCaptor<Advertisement> advertisementArgumentCaptor;
    private SetAsSoldAdvertisementHandler handler;

    @BeforeEach
    void setUp() {
        handler =
                new SetAsSoldAdvertisementHandler(
                        advertisementRepository, userRepository, eventPublisher);
    }

    @Test
    @DisplayName("Should throw exception when user not found")
    void shouldThrowExceptionWhenUserNotFound() {
        // Given
        final SetAsSoldAdvertisementCommand command = getCommand();

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
        final SetAsSoldAdvertisementCommand command = getCommand();
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
        final SetAsSoldAdvertisementCommand command = getCommand();
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
    @DisplayName("Should set as sold")
    @MethodSource("detailsDataProvider")
    void shouldSetAsSold(final AdvertisementDetails<?> details) {
        // Given
        final SetAsSoldAdvertisementCommand command = getCommand();
        final User foundUser = getDummyUserBuilder().build();
        final Advertisement foundAdvertisement =
                spy(getDummyAdvertisementBuilder(details).userId(foundUser.getId()).build());

        given(userRepository.findByEmail(command.userEmail())).willReturn(Optional.of(foundUser));
        given(advertisementRepository.findBySlug(command.slug()))
                .willReturn(Optional.of(foundAdvertisement));

        // When
        handler.handle(command);

        // Then
        verify(advertisementRepository).save(advertisementArgumentCaptor.capture());
        verify(foundAdvertisement).setAsSold();
        verify(eventPublisher).publishEvent(any(AdvertisementStatusChangedEvent.class));
        Assertions.assertThat(advertisementArgumentCaptor.getValue()).isEqualTo(foundAdvertisement);
    }

    private static Stream<Arguments> detailsDataProvider() {
        return Stream.of(
                Arguments.of(getDummyFlatDetails()),
                Arguments.of(getDummyCommercialDetails()),
                Arguments.of(getDummyHouseDetails()),
                Arguments.of(getDummyPlotDetails()));
    }

    private static SetAsSoldAdvertisementCommand getCommand() {
        return new SetAsSoldAdvertisementCommand("abcde", getDummyEmail());
    }
}
