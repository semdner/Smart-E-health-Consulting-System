package com.ehealthsystem.map;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;

import java.io.IOException;
import java.sql.SQLException;

public class GeoDistance {
    public static DistanceMatrix getDistances(String userGeoData, String[] doctorGeoData) throws IOException, InterruptedException, ApiException {
        String[] user = {userGeoData};
        String[] doctors = doctorGeoData;

        // make API request for distance matrix
        return DistanceMatrixApi.getDistanceMatrix(Context.getContext(), user, doctors).await();
    }
}
