package com.ehealthsystem.admin;

import com.ehealthsystem.database.Database;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 *  The class represents a user and is needed when wanting to store a user in an observable.
 */
public class UserTableView {
    /**
     * All the attributes a user can have.
     */
    private SimpleStringProperty username, email, firstName, lastName, street, houseNo, gender;
    private SimpleStringProperty zipCode;
    private SimpleStringProperty birthDate;
    private SimpleStringProperty insuranceName;
    private SimpleBooleanProperty privateInsurance;
    private SimpleStringProperty password;

    /**
     * Construtor for initalizing all the values of a user.
     * @param username the username of the logged in user that is set.
     * @param email the email of the logged in user that is set.
     * @param firstName the first name of the logged in user that is set.
     * @param lastName the last name of the logged in user that is set.
     * @param birthDate the birthday of the logged in user that is set.
     * @param gender the gender of the logged in user that is set.
     * @param street the street of the logged in user that is set.
     * @param houseNo the houseNo of the logged in user that is set.
     * @param zipCode the zip code of the logged in user that is set.
     * @param insuranceName the insurance name of the logged in user that is set.
     * @param privateInsurance the insurance type of the logged in user that is set.
     * @param password the hashed password of the logged in user that is set.
     */
    public UserTableView(String username, String email,  String firstName, String lastName, String birthDate, String gender, String street, String houseNo, String zipCode, String insuranceName, Boolean privateInsurance, String password) {
        this.username = new SimpleStringProperty(username);
        this.email = new SimpleStringProperty(email);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.birthDate = new SimpleStringProperty(birthDate);
        this.gender = new SimpleStringProperty(gender);
        this.street = new SimpleStringProperty(street);
        this.houseNo = new SimpleStringProperty(houseNo);
        this.zipCode = new SimpleStringProperty(zipCode);
        this.insuranceName = new SimpleStringProperty(insuranceName);
        this.privateInsurance = new SimpleBooleanProperty(privateInsurance);
        this.password = new SimpleStringProperty(password);
    }

    /**
     * set the value of the private attribute username.
     * @param username the username of a user.
     */
    public void setUsername(String username) {
        this.username.set(username);
    }

    /**
     * set the value of the private attribute email.
     * @param email the email address of a user.
     */
    public void setEmail(String email) {
        this.email.set(email);
    }

    /**
     * set the value of the private attribute firstName.
     * @param firstName the first name of a user.
     */
    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    /**
     * set the value of the private attribute lastName.
     * @param lastName the last name of a user.
     */
    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    /**
     * set the value of the private attribute birthDate.
     * @param birthDate the birthday of a user.
     */
    public void setBirthDate(String birthDate) {
        this.birthDate.set(birthDate);
    }

    /**
     * set the value of the private attribute gender.
     * @param gender the users gender.
     */
    public void setGender(String gender) {
        this.gender.set(gender);
    }

    /**
     * set the value of the private attribute street.
     * @param street the street the user lives in.
     */
    public void setStreet(String street) {
        this.street.set(street);
    }

    /**
     * set the value of the private attribute houseNo.
     * @param houseNo the house number of the users home.
     */
    public void setHouseNo(String houseNo) {
        this.houseNo.set(houseNo);
    }

    /**
     * set the value of the private attribute zipCode.
     * @param zipCode the zip code of the users home.
     */
    public void setZipCode(String zipCode) {
        this.zipCode.set(zipCode);
    }

    /**
     * set the value of the private attribute insuranceName.
     * @param insuranceName the insurance of the user.
     */
    public void setInsuranceName(String insuranceName) {
        this.insuranceName.set(insuranceName);
    }

    /**
     * set the value of the private attribute privateInsurance.
     * @param privateInsurance the users insurance type.
     */
    public void setPrivateInsurance(Boolean privateInsurance) {
        this.privateInsurance.set(privateInsurance);
    }

    /**
     * set the value of the private attribute password.
     * @param password the users password in plain-text.
     */
    public void setPassword(String password) throws SQLException {
        String query = "UPDATE user SET password = ? WHERE email = ?";
        PreparedStatement statement = Database.connection.prepareStatement(query);
        statement.setString(1, Database.hashPassword(password));
        statement.setString(2, email.get());
        statement.execute();
    }

    /**
     * Returns the username of a user.
     * @return The username as a String.
     */
    public String getUsername() {
        return username.get();
    }

    /**
     * Returns the E-mail of a user.
     * @return The E-mail as a String.
     */
    public String getEmail() {
        return email.get();
    }

    /**
     * Returns the first name of a user.
     * @return The first name as a String.
     */
    public String getFirstName() {
        return firstName.get();
    }

    /**
     * Returns the last name of a user.
     * @return The last name as a String.
     */
    public String getLastName() {
        return lastName.get();
    }

    /**
     * Returns the birthday of a user.
     * @return The birthday as a String.
     */
    public String getBirthDate() {
        return birthDate.get();
    }

    /**
     * Returns the gender of a user.
     * @return The gender as a String.
     */
    public String getGender() {
        return gender.get();
    }

    /**
     * Returns the street where a user lives.
     * @return The street as a String.
     */
    public String getStreet() {
        return street.get();
    }

    /**
     * Returns the house no. of a users home.
     * @return The house no. as a String.
     */
    public String getHouseNo() {
        return houseNo.get();
    }

    /**
     * Returns the zip code of a users home.
     * @return The zip code as a String.
     */
    public String getZipCode() {
        return zipCode.get();
    }

    /**
     * Returns the insurance name of a user.
     * @return The insurance name as a String.
     */
    public String getInsuranceName() {
        return insuranceName.get();
    }

    /**
     * Returns the insurance type of a user.
     * @return The insurance type as a Boolean (false = public, true = private).
     */
    public Boolean getPrivateInsurance() {
        return privateInsurance.get();
    }

    /**
     * Returns the hashed password of a user.
     * @return The password as a String.
     */
    public String getPassword() {
        return password.get();
    }
}
