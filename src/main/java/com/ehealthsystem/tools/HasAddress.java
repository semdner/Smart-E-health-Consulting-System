package com.ehealthsystem.tools;

import com.ehealthsystem.map.Geocoder;
import com.google.maps.errors.ApiException;

import java.io.IOException;
import java.util.Objects;

/**
 *  Implemented by every entity that has an address.
 */
public interface HasAddress {
    String getStreet();
    String getHouseNumber();
    String getZipCode();

    /**
     * Get the formatted address of this entity, containing street, house number, zip code.
     * To also have the place's name at the end, use {@link #getFormattedAddressWithPlaceName()}
     * @return the formatted address of this entity
     */
    default String getFormattedAddress() {
        return "%s %s, %s".formatted(getStreet(), getHouseNumber(), getZipCode());
    }

    /**
     * Get the formatted address of this entity, containing street, house number, zip code, place name.
     * The place name is not stored with entities and is instead determined by geocoding the address with Google Maps.
     * @return the formatted address with place name
     */
    default String getFormattedAddressWithPlaceName() throws IOException, InterruptedException, ApiException {
        return Objects.requireNonNull(Geocoder.geocode(getFormattedAddress())).formattedAddress;
    }
}
