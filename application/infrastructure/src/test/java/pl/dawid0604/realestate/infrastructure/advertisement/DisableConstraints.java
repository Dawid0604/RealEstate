/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure.advertisement;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

@Retention(RUNTIME)
@DisableFlatConstraints
@DisablePlotConstraints
@DisableHouseConstraints
@DisableCommercialConstraints
@interface DisableConstraints {}
