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

import java.util.Optional;
import java.util.stream.Stream;

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

import pl.dawid0604.realestate.application.command.AddAdvertisementPhotoCommand;
import pl.dawid0604.realestate.domain.Advertisement;
import pl.dawid0604.realestate.domain.AdvertisementDetails;
import pl.dawid0604.realestate.domain.AdvertisementPhoto;
import pl.dawid0604.realestate.domain.AdvertisementStatus;
import pl.dawid0604.realestate.domain.User;
import pl.dawid0604.realestate.domain.UserStatus;
import pl.dawid0604.realestate.domain.port.out.AdvertisementRepository;
import pl.dawid0604.realestate.domain.port.out.UserRepository;
import pl.dawid0604.realestate.domain.shared.AdvertisementType;
import pl.dawid0604.realestate.domain.shared.exception.AdvertisementNotFoundException;
import pl.dawid0604.realestate.domain.shared.exception.ForbiddenException;
import pl.dawid0604.realestate.domain.shared.exception.UserNotFoundException;

@ExtendWith(MockitoExtension.class)
class AddAdvertisementPhotoHandlerTest {
    @Mock private AdvertisementRepository advertisementRepository;
    @Mock private UserRepository userRepository;
    @Captor private ArgumentCaptor<Advertisement> advertisementArgumentCaptor;
    private AddAdvertisementPhotoHandler handler;

    @BeforeEach
    void setUp() {
        handler = new AddAdvertisementPhotoHandler(advertisementRepository, userRepository);
    }

    @Test
    @DisplayName("Should throw exception when user not found")
    void shouldThrowExceptionWhenUserNotFound() {
        // Given
        final AddAdvertisementPhotoCommand command = getCommand();

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
        final AddAdvertisementPhotoCommand command = getCommand();
        final User foundUser = getDummyUserBuilder().status(UserStatus.INACTIVE).build();

        given(userRepository.findByEmail(command.userEmail())).willReturn(Optional.of(foundUser));

        // When
        // Then
        Assertions.assertThatThrownBy(() -> handler.handle(command))
                .isExactlyInstanceOf(ForbiddenException.class);

        verify(userRepository, never()).save(any());
        verify(advertisementRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when advertisement not found")
    void shouldThrowExceptionWhenAdvertisementNotFound() {
        // Given
        final AddAdvertisementPhotoCommand command = getCommand();
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
    @DisplayName("Should add photo")
    @MethodSource("detailsDataProvider")
    void shouldAddPhoto(final AdvertisementDetails<?> details) {
        // Given
        final AddAdvertisementPhotoCommand command = getCommand();
        final User foundUser = getDummyUserBuilder().build();
        final Advertisement foundAdvertisement =
                spy(
                        getDummyAdvertisementBuilder(details)
                                .userId(foundUser.getId())
                                .status(AdvertisementStatus.INACTIVE)
                                .build());

        given(userRepository.findByEmail(command.userEmail())).willReturn(Optional.of(foundUser));
        given(advertisementRepository.findBySlug(command.slug(), command.advertisementType()))
                .willReturn(Optional.of(foundAdvertisement));

        // When
        handler.handle(command);

        // Then
        verify(advertisementRepository).save(advertisementArgumentCaptor.capture());
        verify(foundAdvertisement).addPhoto(any());

        Assertions.assertThat(advertisementArgumentCaptor.getValue()).isEqualTo(foundAdvertisement);
        Assertions.assertThat(advertisementArgumentCaptor.getValue().getPhotos())
                .hasSize(1)
                .satisfies(
                        photos -> {
                            final AdvertisementPhoto photo =
                                    photos.toArray(AdvertisementPhoto[]::new)[0];

                            Assertions.assertThat(photo.getUrl().value())
                                    .isEqualTo(command.photoUrl());

                            Assertions.assertThat(photo.getPosition())
                                    .isEqualTo(command.position());
                        });
    }

    private static Stream<Arguments> detailsDataProvider() {
        return Stream.of(
                Arguments.of(getDummyFlatDetails()),
                Arguments.of(getDummyCommercialDetails()),
                Arguments.of(getDummyHouseDetails()),
                Arguments.of(getDummyPlotDetails()));
    }

    private static AddAdvertisementPhotoCommand getCommand() {
        return new AddAdvertisementPhotoCommand(
                "abcde", AdvertisementType.FLAT, "https://photo", 0, getDummyEmail());
    }
}
