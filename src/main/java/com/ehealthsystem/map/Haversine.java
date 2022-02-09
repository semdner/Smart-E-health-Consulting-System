package com.ehealthsystem.map;

import com.google.maps.model.LatLng;

public class Haversine {
    /**
     * Calculate the distance between to location points given as coordinates using the Haversine formula
     * @param loc1 location point 1/A
     * @param loc2 location point 2/B
     * @return distance in kilometers
     */
    public static double distance(LatLng loc1, LatLng loc2) {
        double lat1 = loc1.lat;
        double lon1 = loc1.lng;
        double lat2 = loc2.lat;
        double lon2 = loc2.lng;

        //Thank you, https://www.movable-type.co.uk/scripts/latlong.html (accessed 2022-01-26)
        //The following code is the first JavaScript snippet on the webpage, ported to Java (just data types adjusted)
        long R = (long) 6371e3; //meters
        double phi1 = lat1 * Math.PI/180; // φ, λ in radians
        double phi2 = lat2 * Math.PI/180;
        double deltaPhi = (lat2-lat1) * Math.PI/180;
        double deltaLambda = (lon2-lon1) * Math.PI/180;

        double a = (Math.sin(deltaPhi/2) * Math.sin(deltaPhi/2)) + (Math.cos(phi1) * Math.cos(phi2)) * (Math.sin(deltaLambda/2) * Math.sin(deltaLambda/2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        double d = R * c; // in meters

        return d/1000;
    }

    public static void main(String[] args) {
        System.out.println(distance(
                new LatLng(50.11578, 8.69820),
                new LatLng(50.10746, 8.66423)
        ));
    }
}
