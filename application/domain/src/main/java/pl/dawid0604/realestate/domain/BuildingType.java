/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain;

public sealed interface BuildingType
        permits CommercialBuildingType, FlatBuildingType, HouseBuildingType, PlotBuildingType {}
