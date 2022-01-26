package com.ehealthsystem.map;

import com.google.maps.*;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

import java.io.*;

public class GeoCoder {

    /**
     * Verifies an address
     * At least one parameter must be non-empty
     * @param address consisting of street and optionally including number
     * @param zip the zip code
     * @return if address was found: the entered address as GeocodingResult
     * @throws IOException
     * @throws InterruptedException
     * @throws ApiException
     */
    public static GeocodingResult geocode(String address, String zip) throws IOException, InterruptedException, ApiException {
        if(address.isBlank() && zip.isBlank()) {
            return null;
        }

        System.out.println("GeoCoder: " + address + "," + zip);
        GeocodingResult[] results = GeocodingApi.geocode(Context.getContext(), address + "," + zip).language("de-DE").await();
        return results[0];
    }
}