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
 * The class represents a user of the database
 */
public class User implements HasAddress {
    private String username, email, firstName, lastName, street, houseNo, gender;
    private String zipCode;
    private LocalDate birthDate;
    private String insuranceName;
    private boolean privateInsurance;

    /**
     * Creates a new user object representing a row in the database
     * @param username
     * @param insertIntoDb
     * true if this is a new user that is inserted to the database,
     * false if the object shall solely represent a user that is already saved in the database (i.e. result of a SELECT query)
     * or also false if object is for bridging the step from registration to email validation
     * @param password
     * @param firstName
     * @param lastName
     * @param email
     * @param street
     * @param houseNo
     * @param zipCode
     * @param birthDate
     * @param privateInsurance
     * @throws SQLException
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
     * @param password
     * @throws SQLException
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
     * @param currentPassword
     * @param newPassword
     * @return
     * @throws SQLException
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
     * @param email
     * @param password
     * @throws SQLException
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
     * @param newValues
     * @throws SQLException
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
     * @param username 
     * @throws SQLException
     * @throws UnsupportedDataTypeException
     */
    public void setUsername(String username) throws SQLException, UnsupportedDataTypeException {
        this.username = username;
        update(new Object[][]{{"username", username}});
    }

    /**
     * set and update email in the database
     * @param email
     * @throws SQLException
     * @throws UnsupportedDataTypeException
     */
    public void setEmail(String email) throws SQLException, UnsupportedDataTypeException {
        update(new Object[][]{{"email", email}});
        this.email = email;
    }

    /**
     * set and update the first name in the database
     * @param firstName
     * @throws SQLException
     * @throws UnsupportedDataTypeException
     */
    public void setFirstName(String firstName) throws SQLException, UnsupportedDataTypeException {
        update(new Object[][]{{"first_name", firstName}});
        this.firstName = firstName;
    }

    /**
     * set and update the last name in the database
     * @param lastName
     * @throws SQLException
     * @throws UnsupportedDataTypeException
     */
    public void setLastName(String lastName) throws SQLException, UnsupportedDataTypeException {
        update(new Object[][]{{"last_name", lastName}});
        this.lastName = lastName;
    }

    /**
     * set and update street in the database
     * @param street
     * @throws SQLException
     * @throws UnsupportedDataTypeException
     */
    public void setStreet(String street) throws SQLException, UnsupportedDataTypeException {
        update(new Object[][]{{"street", street}});
        this.street = street;
    }

    /**
     * set and update house no. in the database
     * @param houseNo
     * @throws SQLException
     * @throws UnsupportedDataTypeException
     */
    public void setHouseNo(String houseNo) throws SQLException, UnsupportedDataTypeException {
        update(new Object[][]{{"number", houseNo}});
        this.houseNo = houseNo;
    }

    /**
     * set and update zip code in the database
     * @param zipCode
     * @throws SQLException
     * @throws UnsupportedDataTypeException
     */
    public void setZipCode(String zipCode) throws SQLException, UnsupportedDataTypeException {
        update(new Object[][]{{"zip", zipCode}});
        this.zipCode = zipCode;
    }

    /**
     * set and update the birthday in the database
     * @param birthDate
     * @throws SQLException
     * @throws UnsupportedDataTypeException
     */
    public void setBirthDate(LocalDate birthDate) throws SQLException, UnsupportedDataTypeException {
        String bd = birthDate.format(Database.dateFormatter);
        update(new Object[][]{{"birthday", bd}});
        this.birthDate = birthDate;
    }

    /**
     * set and update the gender in the database
     * @param gender
     * @throws SQLException
     * @throws UnsupportedDataTypeException
     */
    public void setGender(String gender) throws SQLException, UnsupportedDataTypeException {
        update(new Object[][]{{"sex", gender}});
        this.gender = gender;
    }

    /**
     * set and update the insurance name in the database
     * @param insuranceName
     * @throws SQLException
     * @throws UnsupportedDataTypeException
     */
    public void setInsuranceName(String insuranceName) throws SQLException, UnsupportedDataTypeException {
        update(new Object[][]{{"insurance_name", insuranceName}});
        this.insuranceName = insuranceName;
    }

    /**
     * set and update the insurance type in the database
     * @param privateInsurance
     * @throws SQLException
     * @throws UnsupportedDataTypeException
     */
    public void setPrivateInsurance(boolean privateInsurance) throws SQLException, UnsupportedDataTypeException {
        update(new Object[][]{{"private_insurance", privateInsurance}});
        this.privateInsurance = privateInsurance;
    }

    /**
     * get the username returned
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     * get the first name returned
     * @return
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * get the last name returned
     * @return
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * get the mail returned
     * @return
     */
    public String getMail() {
        return email;
    }

    /**
     * get the street returned
     * @return
     */
    public String getStreet() {
        return street;
    }

    /**
     * get the house number returned
     * @return
     */
    public String getHouseNumber() {
        return houseNo;
    }

    /**
     * get the gender returned
     * @return
     */
    public String getGender() {
        return gender;
    }

    /**
     * get the insurance name returned
     * @return
     */
    public String getInsuranceName() {
        return insuranceName;
    }

    /**
     * get the zip returned
     * @return
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * get the birthday returned
     * @return
     */
    public LocalDate getBirthDate() {
        return birthDate;
    }

    /**
     * get the insurance type returned
     * @return
     */
    public boolean isPrivateInsurance() {
        return privateInsurance;
    }

    /**
     * Wrapper to get users appointments from the user object
     * @return usersAppointments
     * @throws SQLException
     */
    public ArrayList<Appointment> getAppointments() throws SQLException, UnsupportedDataTypeException {
        return Database.getUsersAppointments(username); //lazy-load and also don't store
    }
}
