package com.ehealthsystem.map;

import com.ehealthsystem.database.Database;
import com.ehealthsystem.doctor.Doctor;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.LatLng;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;

public class GeoDistance {
    /**
     * Calculate driving distance from one source to several destinations using Google Maps DistanceMatrix API
     * @param userGeoData source address, can be as textual address or in coordinates
     * @param doctorGeoData array of destinations to determine distances to
     * @return the requested distances
     */
    public static DistanceMatrix getDistances(String userGeoData, String[] doctorGeoData) throws IOException, InterruptedException, ApiException {
        String[] user = {userGeoData};
        String[] doctors = doctorGeoData;

        // make API request for distance matrix
        return DistanceMatrixApi.getDistanceMatrix(Context.getContext(), user, doctors).await();
    }

    /**
     * Reduce a list of doctors to only include ones that are in a specific range from the user, determining the distance using Google Maps DistanceMatrix API, requests sent individually
     * @param userGeoData user location
     * @param range maximum distance, in km (DRIVING DISTANCE because Google Maps API)
     * @return a list of doctors that are in the range
     */
    public static ArrayList<DoctorDistance> filterDoctorsInRangeIndividualRequests(ArrayList<Doctor> doctors, String userGeoData, double range) throws IOException, InterruptedException, ApiException {
        ArrayList<DoctorDistance> doctorsInRange = new ArrayList<>();

        for (Doctor d : doctors) {
            double resultDistance = (double)GeoDistance.getDistances(userGeoData, new String[]{d.getFormattedAddress()}).rows[0].elements[0].distance.inMeters/1000;

            if(resultDistance <= range) {
                doctorsInRange.add(new DoctorDistance(resultDistance, d.getFormattedAddress(), d));
            }
        }

        return doctorsInRange;
    }

    /**
     * Reduce a list of doctors to only include ones that are in a specific range from the user, determining the distance using Google Maps DistanceMatrix API, sent in a single batch request
     * @param userGeoData user location
     * @param range maximum distance, in km (DRIVING DISTANCE because Google Maps API)
     * @return a list of doctors that are in the range
     */
    public static ArrayList<DoctorDistance> filterDoctorsInRangeBatchRequest(ArrayList<Doctor> doctors, String userGeoData, double range) throws IOException, InterruptedException, ApiException {
        //Prepare distance request
        //Collect doctors' addresses in a list
        String[] docAddresses = new String[doctors.size()];
        int i = 0;
        for (Doctor d : doctors) {
            //Two ways of giving the doctors location to Google Maps, time to response is the same
            docAddresses[i++] = d.getFormattedAddress(); //saved address
            //docAddresses[i++] = "%s, %s".formatted(d.getLocation().lat, d.getLocation().lng); //coordinates //Only for testing as this string is also used for the DoctorDistance object
        }

        DistanceMatrix d = getDistances(userGeoData, docAddresses);

        ArrayList<DoctorDistance> result = new ArrayList<>();
        //Apply filtering
        //j = current index for element in distance matrix
        //a = current index for doctor in doctors list
        //    could be merged with j but eases algorithm refactoring for potential future merging of classes Doctor and DoctorDistance
        for (int j = 0, a = 0; j < d.rows[0].elements.length; j++) {
            DistanceMatrixElement e = d.rows[0].elements[j];
            double distance = (double)e.distance.inMeters/1000;
            boolean inRange = distance <= range;

            if (inRange) {
                //Add to result set
                Doctor doc = doctors.get(a);
                result.add(new DoctorDistance(distance, docAddresses[j], doc));
            }

            //Increase index for doctors only if not removing the current element
            if (!inRange)
                doctors.remove(a);
            else
                a++;

        }

        return result;
    }

    /**
     * Reduce a list of doctors to only include ones that are in a specific range from the user, determining the distance using local calculation (Haversine formula)
     * @return a list of doctors that are in the range (LINEAR DISTANCE)
     * @throws SQLException see getDoctors()
     */
    public static ArrayList<DoctorDistance> filterDoctorsInRangeWithLocalCalculation(ArrayList<Doctor> doctors, LatLng location, double range) throws SQLException, IOException, InterruptedException, ApiException {
        ArrayList<DoctorDistance> result = new ArrayList<>();

        for (Doctor d : doctors) { //loop through them
            double distance = Haversine.distance(d.getLocation(), location);
            if (distance <= range) //in range
                result.add(new DoctorDistance(
                        distance,
                        d.getFormattedAddress(),
                        d
                ));
        }

        result.sort(Comparator.comparingDouble(o -> o.distance)); //show doctors ordered by distance ascending

        return result;
    }

    public static void main(String[] args) throws SQLException, IOException, InterruptedException, ApiException {
        //Ignore, only for testing algorithms
        //Keep web server caching in mind when looking at the elapsed times

        Database.init();

        long start = System.currentTimeMillis();

        ArrayList<DoctorDistance> a = null;
        String addr = "Alfred-Brehm-Platz 15, 60316";

        //comment out everything but the one you want to measure

        //Methods using Google Maps requests take 1.3 - 5 s in total each
        a = filterDoctorsInRangeIndividualRequests(Database.getDoctors(), addr, 1);
        a = filterDoctorsInRangeBatchRequest(Database.getDoctors(), addr, 1);

        //local calculation is noticeably faster (0-15 ms total time) but UI is somehow still slow:
        a = filterDoctorsInRangeWithLocalCalculation(Database.getDoctors(), new LatLng(50.11645, 8.69815), 1);

        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.printf("Elapsed ms: %s%n", timeElapsed);

        for (DoctorDistance d : a) {
            System.out.println("Distance: " + d.distance);
        }
    }
}
