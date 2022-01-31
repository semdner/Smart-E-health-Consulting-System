package com.ehealthsystem.doctor.specialization;

import com.ehealthsystem.database.Database;

import java.sql.SQLException;
import java.util.ArrayList;

public class Specialization {
    ArrayList<String> specializationList;

    public Specialization() throws SQLException {
        setSpecializationList();
    }

    public void setSpecializationList() throws SQLException {
        specializationList = Database.loadSpecializations();
    }

    public ArrayList<String> getSpecializationList() {
        return specializationList;
    }
}
