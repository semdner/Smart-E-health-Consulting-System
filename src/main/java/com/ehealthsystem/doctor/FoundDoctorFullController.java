package com.ehealthsystem.doctor;

import com.ehealthsystem.map.DoctorDistance;
import com.google.maps.errors.ApiException;
import javafx.fxml.FXML;
import javafx.scene.web.WebView;

import java.io.IOException;

public class FoundDoctorFullController {

    @FXML
    private WebView mapWebView;

    private DoctorDistance doctor = new DoctorDistance();
    private String userGeoData;
    private String doctorGeoData;

    public void start() throws IOException, InterruptedException, ApiException {
        loadPage(userGeoData, doctorGeoData);
        loadDoctorData();
    }

    public void setDoctor(DoctorDistance doctor) {
        this.doctor = doctor;
    }

    public void setUserGeoData(String userGeoData) {
        this.userGeoData = userGeoData;
    }

    public void setDoctorGeoData(String doctorGeoData) {
        this.doctorGeoData = doctorGeoData;
    }

    private void loadDoctorData() {

    }

    private void loadPage(String userGeoData, String doctorGeoData) {

    }
}
