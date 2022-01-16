package com.ehealthsystem.map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLException;

public class GeoDistance {
    public static double getDistance(String userGeoData, String doctorGeoData) throws SQLException, IOException, InterruptedException, ApiException {
        // set API Key
        GeoApiContext context = new GeoApiContext.Builder().apiKey("AIzaSyCUFsJZUQjbl0_0o8DAhQzhMOvxhftI6KQ").build();

        String[] user = {userGeoData};
        String[] doctor = {doctorGeoData};

        // make API request for distance matrix
        DistanceMatrix request = DistanceMatrixApi.getDistanceMatrix(context, user, doctor).await();
        return getDistanceFromJSON(request);
    }

    private static double getDistanceFromJSON(DistanceMatrix request) {
        // Create JSON object from Distance Matrix
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String resultsJson = gson.toJson(request);
        JSONObject json = new JSONObject(resultsJson);

        // Get Distance value from JSON object
        String resultDistance = json.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("distance").getString("humanReadable");

        // remove parts of String for casting
        resultDistance = resultDistance.replace(" km", "");
        return Double.parseDouble(resultDistance);

    }
}
