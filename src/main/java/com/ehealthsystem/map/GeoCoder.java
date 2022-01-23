package com.ehealthsystem.map;

import com.google.maps.*;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

import java.io.*;

public class GeoCoder {
    /**
     * Verifies an address
     * @param address consisting of street and optionally including number
     * @param zip the zip code
     * @return if address was found: the entered address, properly formatted
     * @throws IOException
     * @throws InterruptedException
     * @throws ApiException
     */
    public static String geocodeToFormattedAddress(String address, String zip) throws IOException, InterruptedException, ApiException {
        // set API Key
        GeoApiContext context = new GeoApiContext.Builder().apiKey("AIzaSyCUFsJZUQjbl0_0o8DAhQzhMOvxhftI6KQ").build();

        // geocode address + zip
        if(address.isBlank() && zip.isBlank()) {
            return null;
        }

        GeocodingResult[] results = GeocodingApi.geocode(context,address + "," + zip).language("de-DE").await();
        String formattedAddress = results[0].formattedAddress;
        return formattedAddress;
    }

    public static LatLng geocodeToLatLng(String address) throws IOException, InterruptedException, ApiException {
        // set API Key
        GeoApiContext context = new GeoApiContext.Builder().apiKey("AIzaSyCUFsJZUQjbl0_0o8DAhQzhMOvxhftI6KQ").build();
        GeocodingResult[] results = GeocodingApi.geocode(context,address).language("de-DE").await();
        return results[0].geometry.location;
    }
}