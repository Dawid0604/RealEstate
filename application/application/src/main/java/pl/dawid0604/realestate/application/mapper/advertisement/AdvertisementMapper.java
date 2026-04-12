/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.mapper.advertisement;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import pl.dawid0604.realestate.application.dto.advertisement.AdvertisementDetailsDto;
import pl.dawid0604.realestate.application.dto.advertisement.AdvertisementPhotoDto;
import pl.dawid0604.realestate.application.dto.advertisement.CommercialAdvertisementDetailsDto;
import pl.dawid0604.realestate.application.dto.advertisement.FlatAdvertisementDetailsDto;
import pl.dawid0604.realestate.application.dto.advertisement.HouseAdvertisementDetailsDto;
import pl.dawid0604.realestate.application.dto.advertisement.PlotAdvertisementDetailsDto;
import pl.dawid0604.realestate.application.dto.advertisement.UserCommercialAdvertisementCardDto;
import pl.dawid0604.realestate.application.dto.advertisement.UserFlatAdvertisementCardDto;
import pl.dawid0604.realestate.application.dto.advertisement.UserHouseAdvertisementCardDto;
import pl.dawid0604.realestate.application.dto.advertisement.UserPlotAdvertisementCardDto;
import pl.dawid0604.realestate.domain.shared.projection.advertisement.AdvertisementClaimProjection;
import pl.dawid0604.realestate.domain.shared.projection.advertisement.CommercialAdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.projection.advertisement.CommercialAdvertisementDetailsProjection;
import pl.dawid0604.realestate.domain.shared.projection.advertisement.FlatAdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.projection.advertisement.FlatAdvertisementDetailsProjection;
import pl.dawid0604.realestate.domain.shared.projection.advertisement.HouseAdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.projection.advertisement.HouseAdvertisementDetailsProjection;
import pl.dawid0604.realestate.domain.shared.projection.advertisement.PlotAdvertisementCardProjection;
import pl.dawid0604.realestate.domain.shared.projection.advertisement.PlotAdvertisementDetailsProjection;
import pl.dawid0604.realestate.domain.shared.projection.photo.PhotoProjection;
import pl.dawid0604.realestate.domain.shared.projection.user.AdvertisementUserProjection;

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
    @Mapping(target = "createdAt", source = "projection.createdAt")
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
    @Mapping(target = "createdAt", source = "projection.createdAt")
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
    @Mapping(target = "createdAt", source = "projection.createdAt")
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
    @Mapping(target = "createdAt", source = "projection.createdAt")
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
    @Mapping(target = "createdAt", source = "projection.createdAt")
    @Mapping(target = "isFeatured", source = "projection.featured")
    @Mapping(target = "buildingType", source = "projection.buildingType")
    @Mapping(target = "numberOfRooms", source = "projection.numberOfRooms")
    @Mapping(target = "floor", source = "projection.floor")
    @Mapping(target = "floors", source = "projection.floors")
    @Mapping(target = "builtYear", source = "projection.builtYear")
    UserCommercialAdvertisementCardDto toCommercialCardDto(
            CommercialAdvertisementCardProjection projection,
            String localityFullName,
            Set<PhotoProjection> photos);

    @Mapping(target = "photos", source = "photos", qualifiedByName = "toPhoto")
    @Mapping(target = "slug", source = "projection.slug")
    @Mapping(target = "title", source = "projection.title")
    @Mapping(target = "price", source = "projection.price")
    @Mapping(target = "area", source = "projection.area")
    @Mapping(target = "pricePerSquareMeter", source = "projection.pricePerSquareMeter")
    @Mapping(target = "status", source = "projection.status")
    @Mapping(target = "createdAt", source = "projection.createdAt")
    @Mapping(target = "isFeatured", source = "projection.featured")
    @Mapping(target = "buildingType", source = "projection.buildingType")
    @Mapping(target = "numberOfRooms", source = "projection.numberOfRooms")
    @Mapping(target = "floor", source = "projection.floor")
    @Mapping(target = "floors", source = "projection.floors")
    @Mapping(target = "builtYear", source = "projection.builtYear")
    UserFlatAdvertisementCardDto toFlatCardDto(
            FlatAdvertisementCardProjection projection,
            String localityFullName,
            Set<PhotoProjection> photos);

    @Mapping(target = "photos", source = "photos", qualifiedByName = "toPhoto")
    @Mapping(target = "slug", source = "projection.slug")
    @Mapping(target = "title", source = "projection.title")
    @Mapping(target = "price", source = "projection.price")
    @Mapping(target = "area", source = "projection.area")
    @Mapping(target = "pricePerSquareMeter", source = "projection.pricePerSquareMeter")
    @Mapping(target = "status", source = "projection.status")
    @Mapping(target = "createdAt", source = "projection.createdAt")
    @Mapping(target = "isFeatured", source = "projection.featured")
    @Mapping(target = "buildingType", source = "projection.buildingType")
    @Mapping(target = "numberOfRooms", source = "projection.numberOfRooms")
    @Mapping(target = "floors", source = "projection.floors")
    @Mapping(target = "builtYear", source = "projection.builtYear")
    UserHouseAdvertisementCardDto toHouseCardDto(
            HouseAdvertisementCardProjection projection,
            String localityFullName,
            Set<PhotoProjection> photos);

    @Mapping(target = "photos", source = "photos", qualifiedByName = "toPhoto")
    @Mapping(target = "slug", source = "projection.slug")
    @Mapping(target = "title", source = "projection.title")
    @Mapping(target = "price", source = "projection.price")
    @Mapping(target = "area", source = "projection.area")
    @Mapping(target = "pricePerSquareMeter", source = "projection.pricePerSquareMeter")
    @Mapping(target = "status", source = "projection.status")
    @Mapping(target = "createdAt", source = "projection.createdAt")
    @Mapping(target = "isFeatured", source = "projection.featured")
    @Mapping(target = "plotType", source = "projection.plotType")
    UserPlotAdvertisementCardDto toPlotCardDto(
            PlotAdvertisementCardProjection projection,
            String localityFullName,
            Set<PhotoProjection> photos);

    @Named("toOwner")
    @SuppressWarnings("unused")
    default AdvertisementDetailsDto.Owner toOwner(final AdvertisementUserProjection user) {
        if (user == null) {
            return null;
        }

        return new AdvertisementDetailsDto.Owner(
                user.getId(),
                user.getFirstName() + " " + user.getLastName(),
                user.getUserAvatarUrl(),
                user.getType(),
                user.getContactPhoneNumber(),
                user.getContactEmail());
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
