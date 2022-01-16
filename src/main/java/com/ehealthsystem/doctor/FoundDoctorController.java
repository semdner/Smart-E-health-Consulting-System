package com.ehealthsystem.doctor;

import com.ehealthsystem.database.Database;
import com.ehealthsystem.map.DoctorDistance;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class FoundDoctorController {

    @FXML
    private Label doctorLabel;

    @FXML
    private Label streetLabel;

    @FXML
    private Label houseNoLabel;

    @FXML
    private Label zipLabel;

    public void setData(DoctorDistance doctor) {
        doctorLabel.setText(doctor.getDoctor().getLastName());
        streetLabel.setText(doctor.getDoctor().getStreet());
        houseNoLabel.setText(doctor.getDoctor().getNumber());
        zipLabel.setText(Integer.toString(doctor.getDoctor().getZip()));
    }
}
