package com.ehealthsystem.doctor;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

public class FoundDoctorFullController implements Initializable {

    @FXML
    private WebView mapWebView;
    private WebEngine engine;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        engine = mapWebView.getEngine();
        loadPage();
    }

    public void loadPage() {
        engine.load("https://api.openstreetmap.org");
    }
}
