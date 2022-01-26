package com.ehealthsystem.map;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;

import java.io.IOException;
import java.sql.SQLException;

public class GeoDistance {
    public static double getDistance(String userGeoData, String doctorGeoData) throws SQLException, IOException, InterruptedException, ApiException {
        System.out.println("GeoDistance: " + userGeoData + " <-> " + doctorGeoData);
        String[] user = {userGeoData};
        String[] doctor = {doctorGeoData};

        // make API request for distance matrix
        DistanceMatrix request = DistanceMatrixApi.getDistanceMatrix(Context.getContext(), user, doctor).await();
        return (double)request.rows[0].elements[0].distance.inMeters/1000;
    }
}
