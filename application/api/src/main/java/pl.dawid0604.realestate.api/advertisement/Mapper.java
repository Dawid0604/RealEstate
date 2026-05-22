/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.api.advertisement;

import static java.util.stream.Collectors.toSet;

import pl.dawid0604.realestate.api.advertisement.request.AdvertisementPhotoRequest;
import pl.dawid0604.realestate.application.command.CreateAdvertisementCommand;

import java.util.Set;
import java.util.stream.Stream;

final class Mapper {

    private Mapper() {}

    public static Set<String> mapEnumCollectionToSet(final Set<? extends Enum<?>> collection) {
        return Stream.ofNullable(collection).flatMap(Set::stream).map(Enum::name).collect(toSet());
    }

    public static Set<CreateAdvertisementCommand.AdvertisementPhoto> mapPhotos(
            final Set<AdvertisementPhotoRequest> requestPhotos) {

        return Stream.ofNullable(requestPhotos)
                .flatMap(Set::stream)
                .map(p -> new CreateAdvertisementCommand.AdvertisementPhoto(p.url(), p.position()))
                .collect(toSet());
    }
}
