package com.ehealthsystem.user;

import com.ehealthsystem.appointment.Appointment;
import com.ehealthsystem.database.Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class User {
    private String username, firstName, lastName, mail, street, houseNo;
    private int zipCode;
    private LocalDate birthDate;
    private String preExistingConditions, allergies, pastTreatments, currentTreatments, medications;

    private String insurance;
    private boolean privateInsurance;

    /**
     * Creates a new user object representing a row in the database
     * @param username
     * @param insertIntoDb true if this is a new user that is inserted to the database, false if the object shall solely represent a user that is already saved in the database
     * @param password
     * @param firstName
     * @param lastName
     * @param mail
     * @param street
     * @param houseNo
     * @param zipCode
     * @param birthDate
     * @param preExistingConditions
     * @param allergies
     * @param pastTreatments
     * @param currentTreatments
     * @param medications
     * @param insurance
     * @param privateInsurance
     * @throws SQLException
     */
    public User(String username, boolean insertIntoDb, String password, String firstName, String lastName, String mail, String street, String houseNo, int zipCode, LocalDate birthDate, String preExistingConditions, String allergies, String pastTreatments, String currentTreatments, String medications, String insurance, boolean privateInsurance) throws SQLException {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mail = mail;
        this.street = street;
        this.houseNo = houseNo;
        this.zipCode = zipCode;
        this.birthDate = birthDate;
        this.preExistingConditions = preExistingConditions;
        this.allergies = allergies;
        this.pastTreatments = pastTreatments;
        this.currentTreatments = currentTreatments;
        this.medications = medications;
        this.insurance = insurance;
        this.privateInsurance = privateInsurance;
        if (insertIntoDb)
            insertIntoDb(password);
    }

    /**
     * Inserts this user into the database.
     * Only to be used if a new user is created.
     * @param password
     * @throws SQLException
     */
    private void insertIntoDb(String password) throws SQLException {
        Object[][] parameters = {
                {"username", username},
                {"password", Database.hashPassword(password)},
                {"firstName", firstName},
                {"lastName", lastName},
                {"mail", mail},
                {"street", street},
                {"houseNo", houseNo},
                {"zipCode", zipCode},
                {"birthYear", birthDate.getYear()},
                {"birthMonth", birthDate.getMonthValue()},
                {"birthDay", birthDate.getDayOfMonth()},
                {"preExistingConditions", preExistingConditions},
                {"allergies", allergies},
                {"pastTreatments", pastTreatments},
                {"currentTreatments", currentTreatments},
                {"medications", medications},
                {"insurance", insurance},
                {"privateInsurance", privateInsurance},
        };
        Database.insert("users", parameters);
    }

    /**
     * Change a user's password
     * Used by the user to change their own password
     * @param currentPassword
     * @param newPassword
     * @return
     * @throws SQLException
     */
    public boolean changePassword(String currentPassword, String newPassword) throws SQLException {
        if (!Database.checkPassword(username, currentPassword))
            return false;
        setPassword(username, newPassword);
        return true;
    }

    /**
     * Set a user's password
     * Used as helper function but also by admin, which is why it's public
     * @param username
     * @param password
     * @throws SQLException
     */
    public void setPassword(String username, String password) throws SQLException {
        String query = "UPDATE users SET password = ? WHERE username = ?";
        PreparedStatement statement = Database.connection.prepareStatement(query);
        statement.setString(1, Database.hashPassword(password));
        statement.setString(2, username);
        statement.execute();
    }

    /**
     * Updates this user's entry in the database
     * @param newValues
     * @throws SQLException
     */
    private void update(Object[][] newValues) throws SQLException {
        Database.update(
                "users",
                newValues,
                new Object[][]{{"username", username}}
        );
    }

    public void setFirstName(String firstName) throws SQLException {
        update(new Object[][]{{"firstName", firstName}});
        this.firstName = firstName;
    }

    public void setLastName(String lastName) throws SQLException {
        update(new Object[][]{{"lastName", lastName}});
        this.lastName = lastName;
    }

    public void setStreet(String street) throws SQLException {
        update(new Object[][]{{"street", street}});
        this.street = street;
    }

    public void setHouseNo(String houseNo) throws SQLException {
        update(new Object[][]{{"houseNo", houseNo}});
        this.houseNo = houseNo;
    }

    public void setZipCode(int zipCode) throws SQLException {
        update(new Object[][]{{"zipCode", zipCode}});
        this.zipCode = zipCode;
    }

    public void setBirthDate(LocalDate birthDate) throws SQLException {
        update(new Object[][]{{"birthYear", birthDate.getYear()}, {"birthMonth", birthDate.getMonthValue()}, {"birthDay", birthDate.getDayOfMonth()}});
        this.birthDate = birthDate;
    }

    public void setPreExistingConditions(String preExistingConditions) throws SQLException {
        update(new Object[][]{{"preExistingConditions", preExistingConditions}});
        this.preExistingConditions = preExistingConditions;
    }

    public void setAllergies(String allergies) throws SQLException {
        update(new Object[][]{{"allergies", allergies}});
        this.allergies = allergies;
    }

    public void setPastTreatments(String pastTreatments) throws SQLException {
        update(new Object[][]{{"pastTreatments", pastTreatments}});
        this.pastTreatments = pastTreatments;
    }

    public void setCurrentTreatments(String currentTreatments) throws SQLException {
        update(new Object[][]{{"currentTreatments", currentTreatments}});
        this.currentTreatments = currentTreatments;
    }

    public void setMedications(String medications) throws SQLException {
        update(new Object[][]{{"medications", medications}});
        this.medications = medications;
    }

    public void setInsurance(String insurance) throws SQLException {
        update(new Object[][]{{"insurance", insurance}});
        this.insurance = insurance;
    }

    public void setPrivateInsurance(boolean privateInsurance) throws SQLException {
        update(new Object[][]{{"privateInsurance", privateInsurance}});
        this.privateInsurance = privateInsurance;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMail() {
        return mail;
    }

    public String getStreet() {
        return street;
    }

    public String getHouseNo() {
        return houseNo;
    }

    public int getZipCode() {
        return zipCode;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getPreExistingConditions() {
        return preExistingConditions;
    }

    public String getAllergies() {
        return allergies;
    }

    public String getPastTreatments() {
        return pastTreatments;
    }

    public String getCurrentTreatments() {
        return currentTreatments;
    }

    public String getMedications() {
        return medications;
    }

    public String getInsurance() {
        return insurance;
    }

    public boolean isPrivateInsurance() {
        return privateInsurance;
    }

    /**
     * Wrapper to get users appointments from the user object
     * @return usersAppointments
     * @throws SQLException
     */
    public ArrayList<Appointment> getAppointments() throws SQLException {
        return Database.getUsersAppointments(username);
    }
}
