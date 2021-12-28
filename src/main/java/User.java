import java.sql.SQLException;
import java.time.LocalDate;

public class User {
    private String username, firstName, lastName, mail, street, houseNo;
    private int zipCode;
    private LocalDate birthDate;
    private String preExistingConditions, allergies, pastTreatments, currentTreatments, medications;

    private String insurance;
    private boolean privateInsurance;

    /**
     * Creates a new user that is stored in the database
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

    private void insertIntoDb(String password) throws SQLException {
        Object[][] parameters = {
                {"username", username},
                {"password", DB.hashPassword(password)},
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
        DB.insert("users", parameters);
    }
}
