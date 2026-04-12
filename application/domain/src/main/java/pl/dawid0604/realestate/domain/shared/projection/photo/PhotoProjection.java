/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.domain.shared.projection.photo;

import java.util.UUID;

public interface PhotoProjection {
    UUID getId();

    String getUrl();

    int getPosition();
}
