/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.application.listener;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import pl.dawid0604.realestate.domain.shared.event.AdvertisementPriceChangedEvent;
import pl.dawid0604.realestate.domain.shared.event.AdvertisementStatusChangedEvent;

@Component
class AdvertisementEventListener {

    @EventListener
    void onPriceChanged(final AdvertisementPriceChangedEvent event) {
        throw new NotImplementedException();
    }

    @EventListener
    void onStatusChanged(final AdvertisementStatusChangedEvent event) {
        throw new NotImplementedException();
    }
}
