package com.ehealthsystem.map;

import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;

import java.io.IOException;

public class Geocoder {

    /**
     * Verifies an address
     * At least one parameter must be non-empty
     * @param formattedAddress consisting of street (and optionally including number) and zip code
     * @return if address was found: the entered address as GeocodingResult
     * @throws IOException
     * @throws InterruptedException
     * @throws ApiException
     */
    public static GeocodingResult geocode(String formattedAddress) throws IOException, InterruptedException, ApiException {
        if(formattedAddress.isBlank()) {
            return null;
        }

        System.out.println("Geocoder: " + formattedAddress);
        GeocodingResult[] results = GeocodingApi.geocode(Context.getContext(), formattedAddress).language("de-DE").await();
        return results[0];
    }
}