package com.ehealthsystem.user;

import com.ehealthsystem.appointment.Appointment;
import com.ehealthsystem.database.Database;
import com.ehealthsystem.tools.HasAddress;

import javax.activation.UnsupportedDataTypeException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * The class represents a user and his attributes
 */
public class User implements HasAddress {
    /**
     * Attributes of the user.
     */
    private String username, email, firstName, lastName, street, houseNo, gender;
    private String zipCode;
    private LocalDate birthDate;
    private String insuranceName;
    private boolean privateInsurance;

    /**
     * Creates a new user object representing a row in the database
     * @param username Username of the user.
     * @param password Password chosen by the user.
     * @param firstName First name of the user.
     * @param lastName Last name of the user.
     * @param email E-mail address of the user.
     * @param street Street where the user lives.
     * @param houseNo House no. of the street.
     * @param zipCode Zip code where the user lives.
     * @param birthDate Birthdate of the user.
     * @param privateInsurance Type of insurance.
     * @param insertIntoDb Inserts the user in to the Database.
     *      * true if this is a new user that is inserted to the database,
     *      * false if the object shall solely represent a user that is already saved in the database (i.e. result of a SELECT query)
     *      * or also false if object is for bridging the step from registration to email validation
     * @throws SQLException Throws Exception during connection issues.
     * @throws UnsupportedDataTypeException Throws Exception if the input of data is invalid/in wrong format.
     */
    public User(String username, String email, String firstName, String lastName, String street, String houseNo, String zipCode, LocalDate birthDate, String gender, String password, String insuranceName, boolean privateInsurance, boolean insertIntoDb) throws SQLException, UnsupportedDataTypeException {
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.street = street;
        this.houseNo = houseNo;
        this.zipCode = zipCode;
        this.birthDate = birthDate;
        this.gender = gender;
        this.insuranceName = insuranceName;
        this.privateInsurance = privateInsurance;
        if (insertIntoDb)
            insertIntoDb(password);
    }

    /**
     * Inserts this user into the database.
     * Only to be used if a new user is created.
     * @param password The password in plain-text that is to be stored for this user in a hashed form in the database
     * @throws SQLException Throws Exception during connection issues with the Database.
     * @throws UnsupportedDataTypeException Throws Exception if the input of data is invalid/in wrong format for the database.
     */
    public void insertIntoDb(String password) throws SQLException, UnsupportedDataTypeException {
        Object[][] parameters = {
                {"username", username},
                {"email", email},
                {"first_name", firstName},
                {"last_name", lastName},
                {"street", street},
                {"number", houseNo},
                {"zip", zipCode},
                {"birthday", birthDate},
                {"sex", gender},
                {"password", Database.hashPassword(password)},
                {"insurance_name", insuranceName},
                {"private_insurance", privateInsurance},
        };
        Database.insert("user", parameters);
    }

    /**
     * Change a user's password
     * Used by the user to change their own password
     * @param currentPassword Verifying the current password of the user.
     * @param newPassword Sets a new password if the current password is valid.
     * @return True if the new password is set, false if the process was aborted because current password is wrong.
     * @throws SQLException Throws Exception during connection issues with the Database.
     */
    public boolean changePassword(String currentPassword, String newPassword) throws SQLException {
        if (!Database.checkPassword(email, currentPassword))
            return false;
        setPassword(email, newPassword);
        return true;
    }

    /**
     * Set a user's password
     * Used as helper function but also by admin, which is why it's public
     * @param email The E-mail of the user which password is being set.
     * @param password The password of the user set e.g. by the doctor.
     * @throws SQLException Throws Exception during connection issues with the Database.
     */
    private void setPassword(String email, String password) throws SQLException {
        String query = "UPDATE user SET password = ? WHERE email = ?";
        PreparedStatement statement = Database.connection.prepareStatement(query);
        statement.setString(1, Database.hashPassword(password));
        statement.setString(2, email);
        statement.execute();
    }

    /**
     * Updates this user's entry in the database
     * @param newValues Attributes(values) of the user.
     * @throws SQLException Throws Exception during connection issues with the Database.
     * @throws UnsupportedDataTypeException Throws Exception if the input of data is invalid/in wrong format.
     */
    private void update(Object[][] newValues) throws SQLException, UnsupportedDataTypeException {
        Database.update(
                "user",
                newValues,
                new Object[][]{{"username", username}}
        );
    }

    /**
     * set and update the username in the database
     * @param username The username of the user.
     * @throws SQLException Throws Exception during connection issues with the Database.
     * @throws UnsupportedDataTypeException Throws Exception if the input of data is invalid/in wrong format.
     */
    public void setUsername(String username) throws SQLException, UnsupportedDataTypeException {
        this.username = username;
        update(new Object[][]{{"username", username}});
    }

    /**
     * set and update email in the database
     * @param email The e-mail address of the user-
     * @throws SQLException Throws Exception during connection issues with the Database.
     * @throws UnsupportedDataTypeException Throws Exception if the input of data is invalid/in wrong format.
     */
    public void setEmail(String email) throws SQLException, UnsupportedDataTypeException {
        update(new Object[][]{{"email", email}});
        this.email = email;
    }

    /**
     * set and update the first name in the database
     * @param firstName the first name of the user.
     * @throws SQLException Throws Exception during connection issues with the Database.
     * @throws UnsupportedDataTypeException Throws Exception if the input of data is invalid/in wrong format.
     */
    public void setFirstName(String firstName) throws SQLException, UnsupportedDataTypeException {
        update(new Object[][]{{"first_name", firstName}});
        this.firstName = firstName;
    }

    /**
     * set and update the last name in the database
     * @param lastName The last name of the user.
     * @throws SQLException Throws Exception during connection issues with the Database.
     * @throws UnsupportedDataTypeException Throws Exception if the input of data is invalid/in wrong format.
     */
    public void setLastName(String lastName) throws SQLException, UnsupportedDataTypeException {
        update(new Object[][]{{"last_name", lastName}});
        this.lastName = lastName;
    }

    /**
     * set and update street in the database
     * @param street The street where the user lives.
     * @throws SQLException Throws Exception during connection issues with the Database.
     * @throws UnsupportedDataTypeException Throws Exception if the input of data is invalid/in wrong format.
     */
    public void setStreet(String street) throws SQLException, UnsupportedDataTypeException {
        update(new Object[][]{{"street", street}});
        this.street = street;
    }

    /**
     * set and update house no. in the database
     * @param houseNo The house no. of the street.
     * @throws SQLException Throws Exception during connection issues with the Database.
     * @throws UnsupportedDataTypeException Throws Exception if the input of data is invalid/in wrong format.
     */
    public void setHouseNo(String houseNo) throws SQLException, UnsupportedDataTypeException {
        update(new Object[][]{{"number", houseNo}});
        this.houseNo = houseNo;
    }

    /**
     * set and update zip code in the database
     * @param zipCode The zip code where the user lives.
     * @throws SQLException Throws Exception during connection issues with the Database.
     * @throws UnsupportedDataTypeException Throws Exception if the input of data is invalid/in wrong format.
     */
    public void setZipCode(String zipCode) throws SQLException, UnsupportedDataTypeException {
        update(new Object[][]{{"zip", zipCode}});
        this.zipCode = zipCode;
    }

    /**
     * set and update the birthday in the database
     * @param birthDate The birthday from user.
     * @throws SQLException Throws Exception during connection issues with the Database.
     * @throws UnsupportedDataTypeException Throws Exception if the input of data is invalid/in wrong format.
     */
    public void setBirthDate(LocalDate birthDate) throws SQLException, UnsupportedDataTypeException {
        String bd = birthDate.format(Database.dateFormatter);
        update(new Object[][]{{"birthday", bd}});
        this.birthDate = birthDate;
    }

    /**
     * set and update the gender in the database
     * @param gender The gender of the user.
     * @throws SQLException Throws Exception during connection issues with the Database.
     * @throws UnsupportedDataTypeException Throws Exception if the input of data is invalid/in wrong format.
     */
    public void setGender(String gender) throws SQLException, UnsupportedDataTypeException {
        update(new Object[][]{{"sex", gender}});
        this.gender = gender;
    }

    /**
     * set and update the insurance name in the database
     * @param insuranceName The name of the insurance.
     * @throws SQLException Throws Exception during connection issues with the Database.
     * @throws UnsupportedDataTypeException Throws Exception if the input of data is invalid/in wrong format.
     */
    public void setInsuranceName(String insuranceName) throws SQLException, UnsupportedDataTypeException {
        update(new Object[][]{{"insurance_name", insuranceName}});
        this.insuranceName = insuranceName;
    }

    /**
     * set and update the insurance type in the database
     * @param privateInsurance The type of the Insurance.
     * @throws SQLException Throws Exception during connection issues with the Database.
     * @throws UnsupportedDataTypeException Throws Exception if the input of data is invalid/in wrong format.
     */
    public void setPrivateInsurance(boolean privateInsurance) throws SQLException, UnsupportedDataTypeException {
        update(new Object[][]{{"private_insurance", privateInsurance}});
        this.privateInsurance = privateInsurance;
    }

    /**
     * get the username returned
     * @return The username of the user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * get the first name returned
     * @return The first name of the user.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * get the last name returned
     * @return The last name of the user.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * get the mail returned
     * @return The E-mail of the user.
     */
    public String getMail() {
        return email;
    }

    /**
     * get the street returned
     * @return The street where the user lives.
     */
    public String getStreet() {
        return street;
    }

    /**
     * get the house number returned
     * @return The house no. of the street.
     */
    public String getHouseNumber() {
        return houseNo;
    }

    /**
     * get the gender returned
     * @return The gender of the user.
     */
    public String getGender() {
        return gender;
    }

    /**
     * get the insurance name returned
     * @return The insurance name.
     */
    public String getInsuranceName() {
        return insuranceName;
    }

    /**
     * get the zip returned
     * @return The zip code of the users place of residence.
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * get the birthday returned
     * @return The birthdate of the user as an object of the type LocalDate.
     */
    public LocalDate getBirthDate() {
        return birthDate;
    }

    /**
     * get the insurance type returned
     * @return The type of the insurance of the user.
     */
    public boolean isPrivateInsurance() {
        return privateInsurance;
    }

    /**
     * Wrapper to get users appointments from the user object.
     * @return The appointments of the user.
     * @throws SQLException Throws Exception during connection issues.
     */
    public ArrayList<Appointment> getAppointments() throws SQLException, UnsupportedDataTypeException {
        return Database.getUsersAppointments(username); //lazy-load and also don't store
    }
}
