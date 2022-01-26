package com.ehealthsystem.map;

import com.google.maps.*;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

import java.io.*;

public class GeoCoder {

    static GeoApiContext context = null;

    /**
     * Verifies an address
     * @param address consisting of street and optionally including number
     * @param zip the zip code
     * @return if address was found: the entered address, properly formatted
     * @throws IOException
     * @throws InterruptedException
     * @throws ApiException
     */
    public static GeocodingResult geocode(String address, String zip) throws IOException, InterruptedException, ApiException {
        setupContext();

        // geocode address + zip
        if(address.isBlank() && zip.isBlank()) {
            return null;
        }

        System.out.println("GeoCoder: " + address + "," + zip);
        GeocodingResult[] results = GeocodingApi.geocode(context,address + "," + zip).language("de-DE").await();
        return results[0];
    }

    private static void setupContext() {
        if (context != null)
            return;

        // set API Key
        context = new GeoApiContext.Builder().apiKey("AIzaSyCUFsJZUQjbl0_0o8DAhQzhMOvxhftI6KQ").queryRateLimit(1000).build();
    }

    public static LatLng geocodeToLatLng(String address) throws IOException, InterruptedException, ApiException {
        setupContext();
        GeocodingResult[] results = GeocodingApi.geocode(context,address).language("de-DE").await();
        return results[0].geometry.location;
    }
}