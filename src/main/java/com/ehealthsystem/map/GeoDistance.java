package com.ehealthsystem.map;

import com.ehealthsystem.database.Database;
import com.ehealthsystem.doctor.Doctor;
import com.ehealthsystem.tools.Session;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class GeoDistance {
    public static DistanceMatrix getDistances(String userGeoData, String[] doctorGeoData) throws IOException, InterruptedException, ApiException {
        String[] user = {userGeoData};
        String[] doctors = doctorGeoData;

        // make API request for distance matrix
        return DistanceMatrixApi.getDistanceMatrix(Context.getContext(), user, doctors).await();
    }

    /**
     *
     * @param userGeoData
     * @param range in km
     * @return
     * @throws SQLException
     */
    public static ArrayList<DoctorDistance> getDoctorsInRange(String userGeoData, double range) throws SQLException, IOException, InterruptedException, ApiException {
        ArrayList<Doctor> doctors = Database.getDoctors();

        //Prepare distance request
        //Collect doctors' addresses in a list
        String[] docAddresses = new String[doctors.size()];
        int i = 0;
        for (Doctor d : doctors) {
            //Two ways of giving the doctors location to Google Maps, time to response is the same
            docAddresses[i++] = "%s %s, %s".formatted(d.getStreet(), d.getNumber(), d.getZip()); //saved address
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

    public static ArrayList<DoctorDistance> getDoctorsInRangeWithLocalCalculation() throws SQLException {
        double range = Session.appointment.getDistance();
        ArrayList<Doctor> doctors = Database.getDoctors(); //get doctors
        ArrayList<DoctorDistance> result = new ArrayList<>();

        for (Doctor d : doctors) { //loop through them
            double distance = Haversine.distance(d.getLocation(), Session.userGeo.geometry.location);
            if (distance <= range) //in range
                result.add(new DoctorDistance(
                        distance,
                        "%s %s, %s".formatted(d.getStreet(), d.getNumber(), d.getZip()),
                        d
                ));
        }

        return result;
    }

    public static void main(String[] args) throws SQLException, IOException, InterruptedException, ApiException {
        //Ignore, only for testing algorithms
        //Keep web server caching in mind when looking at the elapsed times

        Database.init();

        long start = System.currentTimeMillis();

        ArrayList<DoctorDistance> a = null;
        String addr = "Alfred-Brehm-Platz 15, 60316";
        if (true) //run new algorithm
            a = Database.getDoctorFromDistance(addr, 1);
        else
            a = getDoctorsInRange(addr, 1);

        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.printf("Elapsed ms: %s%n", timeElapsed);

        for (DoctorDistance d : a) {
            System.out.println("Distance: " + d.distance);
        }
    }
}
