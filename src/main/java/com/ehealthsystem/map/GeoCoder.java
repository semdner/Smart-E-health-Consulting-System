package com.ehealthsystem.map;

import com.ehealthsystem.database.Database;
import com.ehealthsystem.user.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.*;
import com.google.maps.errors.ApiException;
import com.google.maps.errors.RequestDeniedException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.GeocodingResult;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONString;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;

public class GeoCoder {
    public static String geocode(String address, int zip) throws IOException, InterruptedException, ApiException, SQLException {
        // set API Key
        GeoApiContext context = new GeoApiContext.Builder().apiKey("AIzaSyCUFsJZUQjbl0_0o8DAhQzhMOvxhftI6KQ").build();

        // geocode address + zip
        if(address != null || !(zip == 0)) {
            GeocodingResult[] results = GeocodingApi.geocode(context,address + "," + zip).await();
            String formattedAddress = (String)results[0].formattedAddress;
            return formattedAddress;
        }
        return null;
    }
}

// https://maps.googleapis.com/maps/api/distancematrix/json?origins=KarbenerWeg61184Karben&destinations=Gartenstra%C3%9Fe61184Karben&mode=bicycling&language=de-DE&key=AIzaSyCUFsJZUQjbl0_0o8DAhQzhMOvxhftI6KQ
// https://maps.googleapis.com/maps/api/distancematrix/json?origins=40.6655101%2C-73.89188969999998&destinations=40.659569%2C-73.933783&key=AIzaSyCUFsJZUQjbl0_0o8DAhQzhMOvxhftI6KQ