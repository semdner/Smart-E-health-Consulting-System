package com.ehealthsystem.user;

import com.ehealthsystem.appointment.Appointment;
import com.ehealthsystem.database.Database;
import com.ehealthsystem.tools.HasAddress;

import javax.activation.UnsupportedDataTypeException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

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
     * false if the object shall solely represent a user that is already saved in the database
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

    public void setUsername(String username) throws SQLException, UnsupportedDataTypeException {
        this.username = username;
        update(new Object[][]{{"username", username}});
    }

    public void setEmail(String email) throws SQLException, UnsupportedDataTypeException {
        update(new Object[][]{{"email", email}});
        this.email = email;
    }

    public void setFirstName(String firstName) throws SQLException, UnsupportedDataTypeException {
        update(new Object[][]{{"first_name", firstName}});
        this.firstName = firstName;
    }

    public void setLastName(String lastName) throws SQLException, UnsupportedDataTypeException {
        update(new Object[][]{{"last_name", lastName}});
        this.lastName = lastName;
    }

    public void setStreet(String street) throws SQLException, UnsupportedDataTypeException {
        update(new Object[][]{{"street", street}});
        this.street = street;
    }

    public void setHouseNo(String houseNo) throws SQLException, UnsupportedDataTypeException {
        update(new Object[][]{{"number", houseNo}});
        this.houseNo = houseNo;
    }

    public void setZipCode(String zipCode) throws SQLException, UnsupportedDataTypeException {
        update(new Object[][]{{"zip", zipCode}});
        this.zipCode = zipCode;
    }

    public void setBirthDate(LocalDate birthDate) throws SQLException, UnsupportedDataTypeException {
        String bd = birthDate.format(Database.dateFormatter);
        update(new Object[][]{{"birthday", bd}});
        this.birthDate = birthDate;
    }

    public void setGender(String gender) throws SQLException, UnsupportedDataTypeException {
        update(new Object[][]{{"sex", gender}});
        this.gender = gender;
    }

    public void setInsuranceName(String insuranceName) throws SQLException, UnsupportedDataTypeException {
        update(new Object[][]{{"insurance_name", insuranceName}});
        this.insuranceName = insuranceName;
    }

    public void setPrivateInsurance(boolean privateInsurance) throws SQLException, UnsupportedDataTypeException {
        update(new Object[][]{{"private_insurance", privateInsurance}});
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
        return email;
    }

    public String getStreet() {
        return street;
    }

    public String getHouseNumber() {
        return houseNo;
    }

    public String getGender() {
        return gender;
    }

    public String getInsuranceName() {
        return insuranceName;
    }

    public String getZipCode() {
        return zipCode;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public boolean isPrivateInsurance() {
        return privateInsurance;
    }

    public String getHashedPassword(String mail) throws SQLException {
        return Database.getPassword(email);
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
