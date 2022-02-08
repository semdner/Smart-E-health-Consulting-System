package com.ehealthsystem.doctor.specialization;

import com.ehealthsystem.database.Database;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * represents the specializations of a doctor
 */
public class Specialization {
    /**
     * list of all specializations a doctor can have
     */
    ArrayList<String> specializationList;

    /**
     * constructor which calls the set method for the loading information from the db
     * @throws SQLException
     */
    public Specialization() throws SQLException {
        setSpecializationList();
    }

    /**
     * load the specializations from the database
     * @throws SQLException
     */
    public void setSpecializationList() throws SQLException {
        specializationList = Database.loadSpecializations();
    }

    /**
     * get the specializations of a doctor returned
     * @return the list of specializations
     */
    public ArrayList<String> getSpecializationList() {
        return specializationList;
    }
}
