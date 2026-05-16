package pl.dawid0604.realestate.api.advertisement.request;

import io.swagger.v3.oas.annotations.media.Schema;

import pl.dawid0604.realestate.api.validation.ValidArea;
import pl.dawid0604.realestate.api.validation.ValidBuildingType;
import pl.dawid0604.realestate.api.validation.ValidBuiltYear;
import pl.dawid0604.realestate.api.validation.ValidEmail;
import pl.dawid0604.realestate.api.validation.ValidFloor;
import pl.dawid0604.realestate.api.validation.ValidFloors;
import pl.dawid0604.realestate.api.validation.ValidLocalityId;
import pl.dawid0604.realestate.api.validation.ValidNumberOfRooms;
import pl.dawid0604.realestate.api.validation.ValidPrice;
import pl.dawid0604.realestate.api.validation.ValidTitle;
import pl.dawid0604.realestate.api.validation.ValidTypeOfMarket;
import pl.dawid0604.realestate.domain.FlatBuildingType;
import pl.dawid0604.realestate.domain.TypeOfMarket;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Schema(description = "Create flat advertisement action")
public record CreateFlatAdvertisementRequest(
        @Schema(description = "Advertisement title", example = "Any exampled title") @ValidTitle
                String title,
        @Schema(description = "Advertisement description", example = "Any exampled description")
                @ValidTitle
                String description,
        @Schema(description = "Advertisement price", example = "250000") @ValidPrice
                BigDecimal price,
        @Schema(
                        description = "Advertisement locality",
                        example = "019e2325-d92b-70ad-94e3-609123e34a79")
                @ValidLocalityId
                UUID localityId,
        @Schema(description = "User email", example = "anyMail@mail.com") @ValidEmail
                String userEmail,
        @Schema(description = "Advertisement number of rooms", example = "3") @ValidNumberOfRooms
                Integer numberOfRooms,
        @Schema(description = "Advertisement floor", example = "4") @ValidFloor Integer floor,
        @Schema(description = "Advertisement floors", example = "5") @ValidFloors Integer floors,
        @Schema(description = "Advertisement built year", example = "1998") @ValidBuiltYear
                Integer builtYear,
        @Schema(description = "Advertisement type of market") @ValidTypeOfMarket
                TypeOfMarket typeOfMarket,
        @Schema(description = "Advertisement photos") Set<AdvertisementPhotoRequest> photos,
        @Schema(description = "Advertisement building type") @ValidBuildingType
                FlatBuildingType buildingType,
        @Schema(description = "Advertisement area", example = "100.25") @ValidArea BigDecimal area,
        @Schema(description = "Advertisement remaining data") Map<String, String> claims,
        @Schema(description = "Advertisement featured state", example = "true") Boolean featured) {}
