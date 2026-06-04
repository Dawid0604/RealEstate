/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared.advertisement.projection;

import pl.dawid0604.realestate.domain.PlotBuildingType;

public non-sealed interface PlotAdvertisementDetailsProjection
        extends AdvertisementDetailsProjection {

    PlotBuildingType getPlotType();
}
