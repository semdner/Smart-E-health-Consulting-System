package com.ehealthsystem.user;

import com.ehealthsystem.appointment.Appointment;
import com.ehealthsystem.database.Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class User {
    private String username, email, firstName, lastName, street, houseNo, gender;
    private int zipCode;
    private LocalDate birthDate;
    private boolean privateInsurance;

    /**
     * Creates a new user object representing a row in the database
     * @param username
     * @param insertIntoDb true if this is a new user that is inserted to the database, false if the object shall solely represent a user that is already saved in the database
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
    public User(String username, String email, String firstName, String lastName, String street, String houseNo, int zipCode, LocalDate birthDate, String gender, String password, boolean privateInsurance, boolean insertIntoDb) throws SQLException {
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.street = street;
        this.houseNo = houseNo;
        this.zipCode = zipCode;
        this.birthDate = birthDate;
        this.gender = gender;
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
                {"email", email},
                {"first_name", firstName},
                {"last_name", lastName},
                {"birthday", birthDate},
                {"street", street},
                {"number", houseNo},
                {"zipCode", zipCode},
                {"sex", gender},
                {"password", Database.hashPassword(password)},
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
        return email;
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
