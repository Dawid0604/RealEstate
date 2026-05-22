/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.mapper.advertisement;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import pl.dawid0604.realestate.application.dto.advertisement.AdvertisementDetailsDto;
import pl.dawid0604.realestate.application.dto.advertisement.AdvertisementPhotoDto;
import pl.dawid0604.realestate.application.dto.advertisement.CommercialAdvertisementCardDto;
import pl.dawid0604.realestate.application.dto.advertisement.CommercialAdvertisementDetailsDto;
import pl.dawid0604.realestate.application.dto.advertisement.FlatAdvertisementCardDto;
import pl.dawid0604.realestate.application.dto.advertisement.FlatAdvertisementDetailsDto;
import pl.dawid0604.realestate.application.dto.advertisement.HouseAdvertisementCardDto;
import pl.dawid0604.realestate.application.dto.advertisement.HouseAdvertisementDetailsDto;
import pl.dawid0604.realestate.application.dto.advertisement.PlotAdvertisementCardDto;
import pl.dawid0604.realestate.application.dto.advertisement.PlotAdvertisementDetailsDto;
import pl.dawid0604.realestate.application.dto.advertisement.UserCommercialAdvertisementCardDto;
import pl.dawid0604.realestate.application.dto.advertisement.UserFlatAdvertisementCardDto;
import pl.dawid0604.realestate.application.dto.advertisement.UserHouseAdvertisementCardDto;
import pl.dawid0604.realestate.application.dto.advertisement.UserPlotAdvertisementCardDto;
import pl.dawid0604.realestate.domain.UserType;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.AdvertisementClaimProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.CommercialAdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.CommercialAdvertisementDetailsProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.FlatAdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.FlatAdvertisementDetailsProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.HouseAdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.HouseAdvertisementDetailsProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.PlotAdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.PlotAdvertisementDetailsProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.UserCommercialAdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.UserFlatAdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.UserHouseAdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.advertisement.projection.UserPlotAdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.photo.projection.PhotoProjection;
import pl.dawid0604.realestate.domain.shared.user.projection.AdvertisementUserProjection;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@SuppressWarnings("CPD-START")
@Mapper(componentModel = "spring")
public interface AdvertisementMapper {

    @Mapping(target = "owner", source = "user", qualifiedByName = "toOwner")
    @Mapping(target = "photos", source = "photos", qualifiedByName = "toPhoto")
    @Mapping(target = "claims", source = "claims", qualifiedByName = "toClaim")
    @Mapping(target = "slug", source = "projection.slug")
    @Mapping(target = "title", source = "projection.title")
    @Mapping(target = "description", source = "projection.description")
    @Mapping(target = "price", source = "projection.price")
    @Mapping(target = "area", source = "projection.area")
    @Mapping(target = "pricePerSquareMeter", source = "projection.pricePerSquareMeter")
    @Mapping(target = "status", source = "projection.status")
    @Mapping(target = "createdAt", source = "projection.createdAt", qualifiedByName = "formatDate")
    @Mapping(target = "isFeatured", source = "projection.featured")
    @Mapping(target = "buildingType", source = "projection.buildingType")
    @Mapping(target = "numberOfRooms", source = "projection.numberOfRooms")
    @Mapping(target = "floor", source = "projection.floor")
    @Mapping(target = "floors", source = "projection.floors")
    @Mapping(target = "builtYear", source = "projection.builtYear")
    FlatAdvertisementDetailsDto toFlatDetailsDto(
            FlatAdvertisementDetailsProjection projection,
            String localityFullName,
            Set<PhotoProjection> photos,
            Set<AdvertisementClaimProjection> claims,
            AdvertisementUserProjection user);

    @Mapping(target = "owner", source = "user", qualifiedByName = "toOwner")
    @Mapping(target = "photos", source = "photos", qualifiedByName = "toPhoto")
    @Mapping(target = "claims", source = "claims", qualifiedByName = "toClaim")
    @Mapping(target = "slug", source = "projection.slug")
    @Mapping(target = "title", source = "projection.title")
    @Mapping(target = "description", source = "projection.description")
    @Mapping(target = "price", source = "projection.price")
    @Mapping(target = "area", source = "projection.area")
    @Mapping(target = "pricePerSquareMeter", source = "projection.pricePerSquareMeter")
    @Mapping(target = "status", source = "projection.status")
    @Mapping(target = "createdAt", source = "projection.createdAt", qualifiedByName = "formatDate")
    @Mapping(target = "isFeatured", source = "projection.featured")
    @Mapping(target = "buildingType", source = "projection.buildingType")
    @Mapping(target = "numberOfRooms", source = "projection.numberOfRooms")
    @Mapping(target = "floors", source = "projection.floors")
    @Mapping(target = "builtYear", source = "projection.builtYear")
    HouseAdvertisementDetailsDto toHouseDetailsDto(
            HouseAdvertisementDetailsProjection projection,
            String localityFullName,
            Set<PhotoProjection> photos,
            Set<AdvertisementClaimProjection> claims,
            AdvertisementUserProjection user);

    @Mapping(target = "owner", source = "user", qualifiedByName = "toOwner")
    @Mapping(target = "photos", source = "photos", qualifiedByName = "toPhoto")
    @Mapping(target = "claims", source = "claims", qualifiedByName = "toClaim")
    @Mapping(target = "slug", source = "projection.slug")
    @Mapping(target = "title", source = "projection.title")
    @Mapping(target = "description", source = "projection.description")
    @Mapping(target = "price", source = "projection.price")
    @Mapping(target = "area", source = "projection.area")
    @Mapping(target = "pricePerSquareMeter", source = "projection.pricePerSquareMeter")
    @Mapping(target = "status", source = "projection.status")
    @Mapping(target = "createdAt", source = "projection.createdAt", qualifiedByName = "formatDate")
    @Mapping(target = "isFeatured", source = "projection.featured")
    @Mapping(target = "buildingType", source = "projection.buildingType")
    @Mapping(target = "numberOfRooms", source = "projection.numberOfRooms")
    @Mapping(target = "floor", source = "projection.floor")
    @Mapping(target = "floors", source = "projection.floors")
    @Mapping(target = "builtYear", source = "projection.builtYear")
    CommercialAdvertisementDetailsDto toCommercialDetailsDto(
            CommercialAdvertisementDetailsProjection projection,
            String localityFullName,
            Set<PhotoProjection> photos,
            Set<AdvertisementClaimProjection> claims,
            AdvertisementUserProjection user);

    @Mapping(target = "owner", source = "user", qualifiedByName = "toOwner")
    @Mapping(target = "photos", source = "photos", qualifiedByName = "toPhoto")
    @Mapping(target = "claims", source = "claims", qualifiedByName = "toClaim")
    @Mapping(target = "slug", source = "projection.slug")
    @Mapping(target = "title", source = "projection.title")
    @Mapping(target = "description", source = "projection.description")
    @Mapping(target = "price", source = "projection.price")
    @Mapping(target = "area", source = "projection.area")
    @Mapping(target = "pricePerSquareMeter", source = "projection.pricePerSquareMeter")
    @Mapping(target = "status", source = "projection.status")
    @Mapping(target = "createdAt", source = "projection.createdAt", qualifiedByName = "formatDate")
    @Mapping(target = "isFeatured", source = "projection.featured")
    @Mapping(target = "plotType", source = "projection.plotType")
    PlotAdvertisementDetailsDto toPlotDetailsDto(
            PlotAdvertisementDetailsProjection projection,
            String localityFullName,
            Set<PhotoProjection> photos,
            Set<AdvertisementClaimProjection> claims,
            AdvertisementUserProjection user);

    @Mapping(target = "photos", source = "photos", qualifiedByName = "toPhoto")
    @Mapping(target = "slug", source = "projection.slug")
    @Mapping(target = "title", source = "projection.title")
    @Mapping(target = "price", source = "projection.price")
    @Mapping(target = "area", source = "projection.area")
    @Mapping(target = "pricePerSquareMeter", source = "projection.pricePerSquareMeter")
    @Mapping(target = "status", source = "projection.status")
    @Mapping(target = "createdAt", source = "projection.createdAt", qualifiedByName = "formatDate")
    @Mapping(target = "isFeatured", source = "projection.featured")
    @Mapping(target = "buildingType", source = "projection.buildingType")
    @Mapping(target = "numberOfRooms", source = "projection.numberOfRooms")
    @Mapping(target = "floor", source = "projection.floor")
    @Mapping(target = "floors", source = "projection.floors")
    @Mapping(target = "builtYear", source = "projection.builtYear")
    UserCommercialAdvertisementCardDto toUserCommercialCardDto(
            UserCommercialAdvertisementCardProjection projection,
            String localityFullName,
            Set<PhotoProjection> photos);

    @Mapping(target = "photos", source = "photos", qualifiedByName = "toPhoto")
    @Mapping(target = "slug", source = "projection.slug")
    @Mapping(target = "userType", source = "userType")
    @Mapping(target = "title", source = "projection.title")
    @Mapping(target = "price", source = "projection.price")
    @Mapping(target = "area", source = "projection.area")
    @Mapping(target = "pricePerSquareMeter", source = "projection.pricePerSquareMeter")
    @Mapping(target = "status", source = "projection.status")
    @Mapping(target = "createdAt", source = "projection.createdAt", qualifiedByName = "formatDate")
    @Mapping(target = "isFeatured", source = "projection.featured")
    @Mapping(target = "buildingType", source = "projection.buildingType")
    @Mapping(target = "numberOfRooms", source = "projection.numberOfRooms")
    @Mapping(target = "floor", source = "projection.floor")
    @Mapping(target = "floors", source = "projection.floors")
    @Mapping(target = "builtYear", source = "projection.builtYear")
    CommercialAdvertisementCardDto toCommercialCardDto(
            CommercialAdvertisementCardProjection projection,
            String localityFullName,
            Set<PhotoProjection> photos,
            UserType userType);

    @Mapping(target = "photos", source = "photos", qualifiedByName = "toPhoto")
    @Mapping(target = "slug", source = "projection.slug")
    @Mapping(target = "title", source = "projection.title")
    @Mapping(target = "price", source = "projection.price")
    @Mapping(target = "area", source = "projection.area")
    @Mapping(target = "pricePerSquareMeter", source = "projection.pricePerSquareMeter")
    @Mapping(target = "status", source = "projection.status")
    @Mapping(target = "createdAt", source = "projection.createdAt", qualifiedByName = "formatDate")
    @Mapping(target = "isFeatured", source = "projection.featured")
    @Mapping(target = "buildingType", source = "projection.buildingType")
    @Mapping(target = "numberOfRooms", source = "projection.numberOfRooms")
    @Mapping(target = "floor", source = "projection.floor")
    @Mapping(target = "floors", source = "projection.floors")
    @Mapping(target = "builtYear", source = "projection.builtYear")
    UserFlatAdvertisementCardDto toUserFlatCardDto(
            UserFlatAdvertisementCardProjection projection,
            String localityFullName,
            Set<PhotoProjection> photos);

    @Mapping(target = "photos", source = "photos", qualifiedByName = "toPhoto")
    @Mapping(target = "userType", source = "userType")
    @Mapping(target = "slug", source = "projection.slug")
    @Mapping(target = "title", source = "projection.title")
    @Mapping(target = "price", source = "projection.price")
    @Mapping(target = "area", source = "projection.area")
    @Mapping(target = "pricePerSquareMeter", source = "projection.pricePerSquareMeter")
    @Mapping(target = "status", source = "projection.status")
    @Mapping(target = "createdAt", source = "projection.createdAt", qualifiedByName = "formatDate")
    @Mapping(target = "isFeatured", source = "projection.featured")
    @Mapping(target = "buildingType", source = "projection.buildingType")
    @Mapping(target = "numberOfRooms", source = "projection.numberOfRooms")
    @Mapping(target = "floor", source = "projection.floor")
    @Mapping(target = "floors", source = "projection.floors")
    @Mapping(target = "builtYear", source = "projection.builtYear")
    FlatAdvertisementCardDto toFlatCardDto(
            FlatAdvertisementCardProjection projection,
            String localityFullName,
            Set<PhotoProjection> photos,
            UserType userType);

    @Mapping(target = "photos", source = "photos", qualifiedByName = "toPhoto")
    @Mapping(target = "slug", source = "projection.slug")
    @Mapping(target = "title", source = "projection.title")
    @Mapping(target = "price", source = "projection.price")
    @Mapping(target = "area", source = "projection.area")
    @Mapping(target = "pricePerSquareMeter", source = "projection.pricePerSquareMeter")
    @Mapping(target = "status", source = "projection.status")
    @Mapping(target = "createdAt", source = "projection.createdAt", qualifiedByName = "formatDate")
    @Mapping(target = "isFeatured", source = "projection.featured")
    @Mapping(target = "buildingType", source = "projection.buildingType")
    @Mapping(target = "numberOfRooms", source = "projection.numberOfRooms")
    @Mapping(target = "floors", source = "projection.floors")
    @Mapping(target = "builtYear", source = "projection.builtYear")
    UserHouseAdvertisementCardDto toUserHouseCardDto(
            UserHouseAdvertisementCardProjection projection,
            String localityFullName,
            Set<PhotoProjection> photos);

    @Mapping(target = "photos", source = "photos", qualifiedByName = "toPhoto")
    @Mapping(target = "userType", source = "userType")
    @Mapping(target = "slug", source = "projection.slug")
    @Mapping(target = "title", source = "projection.title")
    @Mapping(target = "price", source = "projection.price")
    @Mapping(target = "area", source = "projection.area")
    @Mapping(target = "pricePerSquareMeter", source = "projection.pricePerSquareMeter")
    @Mapping(target = "status", source = "projection.status")
    @Mapping(target = "createdAt", source = "projection.createdAt", qualifiedByName = "formatDate")
    @Mapping(target = "isFeatured", source = "projection.featured")
    @Mapping(target = "buildingType", source = "projection.buildingType")
    @Mapping(target = "numberOfRooms", source = "projection.numberOfRooms")
    @Mapping(target = "floors", source = "projection.floors")
    @Mapping(target = "builtYear", source = "projection.builtYear")
    HouseAdvertisementCardDto toHouseCardDto(
            HouseAdvertisementCardProjection projection,
            String localityFullName,
            Set<PhotoProjection> photos,
            UserType userType);

    @Mapping(target = "photos", source = "photos", qualifiedByName = "toPhoto")
    @Mapping(target = "slug", source = "projection.slug")
    @Mapping(target = "title", source = "projection.title")
    @Mapping(target = "price", source = "projection.price")
    @Mapping(target = "area", source = "projection.area")
    @Mapping(target = "pricePerSquareMeter", source = "projection.pricePerSquareMeter")
    @Mapping(target = "status", source = "projection.status")
    @Mapping(target = "createdAt", source = "projection.createdAt", qualifiedByName = "formatDate")
    @Mapping(target = "isFeatured", source = "projection.featured")
    @Mapping(target = "plotType", source = "projection.plotType")
    UserPlotAdvertisementCardDto toUserPlotCardDto(
            UserPlotAdvertisementCardProjection projection,
            String localityFullName,
            Set<PhotoProjection> photos);

    @Mapping(target = "photos", source = "photos", qualifiedByName = "toPhoto")
    @Mapping(target = "userType", source = "userType")
    @Mapping(target = "slug", source = "projection.slug")
    @Mapping(target = "title", source = "projection.title")
    @Mapping(target = "price", source = "projection.price")
    @Mapping(target = "area", source = "projection.area")
    @Mapping(target = "pricePerSquareMeter", source = "projection.pricePerSquareMeter")
    @Mapping(target = "status", source = "projection.status")
    @Mapping(target = "createdAt", source = "projection.createdAt", qualifiedByName = "formatDate")
    @Mapping(target = "isFeatured", source = "projection.featured")
    @Mapping(target = "plotType", source = "projection.plotType")
    PlotAdvertisementCardDto toPlotCardDto(
            PlotAdvertisementCardProjection projection,
            String localityFullName,
            Set<PhotoProjection> photos,
            UserType userType);

    @Named("formatDate")
    @SuppressWarnings("unused")
    default String formatDate(final Instant datetime) {
        if (datetime == null) {
            return null;
        }

        final DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault());

        return formatter.format(datetime);
    }

    @Named("toOwner")
    @SuppressWarnings("unused")
    default AdvertisementDetailsDto.Owner toOwner(final AdvertisementUserProjection user) {
        if (user == null) {
            return null;
        }

        String fullName = null;

        if (user.getFirstName() != null && user.getLastName() != null) {
            fullName = user.getFirstName().trim() + " " + user.getLastName().trim();

        } else if (user.getFirstName() != null) {
            fullName = user.getFirstName().strip();

        } else if (user.getLastName() != null) {
            fullName = user.getLastName().strip();
        }

        return new AdvertisementDetailsDto.Owner(
                user.getId(),
                fullName,
                user.getAvatarUrl(),
                user.getType().name(),
                user.getNotificationPhoneNumber(),
                user.getNotificationEmail());
    }

    @Named("toPhoto")
    @SuppressWarnings("unused")
    default AdvertisementPhotoDto toPhoto(final PhotoProjection projection) {
        if (projection == null) {
            return null;
        }

        return new AdvertisementPhotoDto(
                projection.getId(), projection.getUrl(), projection.getPosition());
    }

    @Named("toClaim")
    @SuppressWarnings("unused")
    default AdvertisementDetailsDto.Claim toClaim(final AdvertisementClaimProjection projection) {
        if (projection == null) {
            return null;
        }

        return new AdvertisementDetailsDto.Claim(
                projection.getClaimKey(), projection.getClaimValue());
    }
}
