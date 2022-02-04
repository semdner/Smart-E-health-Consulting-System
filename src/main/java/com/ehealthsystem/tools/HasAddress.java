package com.ehealthsystem.tools;

import com.ehealthsystem.map.GeoCoder;
import com.google.maps.errors.ApiException;

import java.io.IOException;

public interface HasAddress {
    String getStreet();
    String getHouseNumber();
    String getZipCode();

    default String getFormattedAddress() {
        return "%s %s, %s".formatted(getStreet(), getHouseNumber(), getZipCode());
    }

    default String getFormattedAddressWithPlaceName() throws IOException, InterruptedException, ApiException {
        return GeoCoder.geocode(getFormattedAddress()).formattedAddress;
    }
}
