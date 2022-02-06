package com.ehealthsystem.admin;

import com.ehealthsystem.database.Database;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class UserTableView {
    private SimpleStringProperty username, email, firstName, lastName, street, houseNo, gender;
    private SimpleStringProperty zipCode;
    private SimpleStringProperty birthDate;
    private SimpleStringProperty insuranceName;
    private SimpleBooleanProperty privateInsurance;
    private SimpleStringProperty password;

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

    public void setUsername(String username) {
        this.username.set(username);
    }
    public void setEmail(String email) {
        this.email.set(email);
    }
    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }
    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }
    public void setBirthDate(String birthDate) {
        this.birthDate.set(birthDate);
    }
    public void setGender(String gender) {
        this.gender.set(gender);
    }
    public void setStreet(String street) {
        this.street.set(street);
    }
    public void setHouseNo(String houseNo) {
        this.houseNo.set(houseNo);
    }
    public void setZipCode(String zipCode) {
        this.zipCode.set(zipCode);
    }
    public void setInsuranceName(String insuranceName) {
        this.insuranceName.set(insuranceName);
    }
    public void setPrivateInsurance(Boolean privateInsurance) {
        this.privateInsurance.set(privateInsurance);
    }
    public void setPassword(String password) throws SQLException {
        String query = "UPDATE user SET password = ? WHERE email = ?";
        PreparedStatement statement = Database.connection.prepareStatement(query);
        statement.setString(1, Database.hashPassword(password));
        statement.setString(2, email.get());
        statement.execute();
    }


    public String getUsername() {
        return username.get();
    }
    public String getEmail() {
        return email.get();
    }
    public String getFirstName() {
        return firstName.get();
    }
    public String getLastName() {
        return lastName.get();
    }
    public String getBirthDate() {
        return birthDate.get();
    }
    public String getGender() {
        return gender.get();
    }
    public String getStreet() {
        return street.get();
    }
    public String getHouseNo() {
        return houseNo.get();
    }
    public String getZipCode() {
        return zipCode.get();
    }
    public String getInsuranceName() {
        return insuranceName.get();
    }
    public Boolean getPrivateInsurance() {
        return privateInsurance.get();
    }
    public String getPassword() {
        return password.get();
    }

}
