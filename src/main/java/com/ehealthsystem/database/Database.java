package com.ehealthsystem.database;

import com.ehealthsystem.appointment.Appointment;
import com.ehealthsystem.doctor.Doctor;
import com.ehealthsystem.doctor.DoctorTimeSlot;
import com.ehealthsystem.healthinformation.HealthInformation;
import com.ehealthsystem.map.DoctorDistance;
import com.ehealthsystem.map.GeoDistance;
import com.ehealthsystem.tools.ResourceReader;
import com.ehealthsystem.user.User;
import com.google.maps.errors.ApiException;
import com.google.maps.model.LatLng;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.sql.SQLException;

public class Database {

    public static Connection connection = null;
    public static final String fileName = "ehealth.sqlite3";
    public static final String datePattern = "yyyy-MM-dd"; //date pattern used for tables (e.g. appointment, user)
    public static final String timePatternAppointment = "HH:mm"; //time pattern used for user appointment

    /**
     * To be called on application start.
     * Establishes connection to the database.
     * Creates a database if it doesn't exist yet.
     * Before creating a database, it asks for an admin password.
     */
    public static void init()
    {
        try
        {
            connection = DriverManager.getConnection("jdbc:sqlite:" + fileName); //creates file if it doesn't exist
            connection.createStatement().execute("PRAGMA foreign_keys = ON;"); //require foreign key integrity checks //needs to run on every driver setup (session only preference)
        }
        catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
    }

    /**
     * Create database with provided admin password
     * @param initialAdminPassword provided admin password desired by the user
     * @throws SQLException
     */
    public static void createDB(String initialAdminPassword) throws SQLException {
        init();
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);  // set timeout to 30 sec.

        //Create tables
        statement.execute(ResourceReader.getResourceString("database/createTableMedication.sql"));
        statement.execute(ResourceReader.getResourceString("database/createTableDisease.sql"));
        statement.execute(ResourceReader.getResourceString("database/createTableLocation.sql"));
        statement.execute(ResourceReader.getResourceString("database/createTableCategory.sql"));
        statement.execute(ResourceReader.getResourceString("database/createTableDoctor.sql"));
        statement.execute(ResourceReader.getResourceString("database/createTableDoctorCategory.sql"));
        statement.execute(ResourceReader.getResourceString("database/createTableDoctorAppointment.sql"));
        statement.execute(ResourceReader.getResourceString("database/createTableUser.sql"));
        statement.execute(ResourceReader.getResourceString("database/createTablePrescription.sql"));
        statement.execute(ResourceReader.getResourceString("database/createTableHealthStatus.sql"));
        statement.execute(ResourceReader.getResourceString("database/createTableAppointment.sql"));

        statement.execute(ResourceReader.getResourceString("database/insertIntoDisease.sql"));
        statement.execute(ResourceReader.getResourceString("database/InsertIntoLocation.sql"));
        statement.execute(ResourceReader.getResourceString("database/insertIntoMedication.sql"));

        //dummy data
        //statement.execute(ResourceReader.getResourceString("database/insertIntoPrescription.sql"));
        //statement.execute(ResourceReader.getResourceString("database/insertIntoHealthStatus.sql"));

        statement.execute(ResourceReader.getResourceString("database/insertIntoCategory.sql"));
        statement.execute(ResourceReader.getResourceString("database/insertIntoDoctor.sql"));
        statement.execute(ResourceReader.getResourceString("database/insertIntoDoctorCategory.sql"));
        statement.execute(ResourceReader.getResourceString("database/insertIntoDoctorAppointment.sql"));

        //Insert admin user
        String query = "INSERT INTO user (username, password) VALUES ('admin', ?)";
        PreparedStatement adminInsert = connection.prepareStatement(query);
        adminInsert.setString(1, hashPassword(initialAdminPassword));
        adminInsert.execute();
    }

    /**
     * Helper method to generate a password's hash
     * @param password the password to hash
     * @return hash
     */
    public static String hashPassword(String password)
    {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    /**
     * Properly closes the connection to the database
     * Probably only needed if a big query would still be running
     */
    public static void close()
    {
        try
        {
            if(connection != null)
                connection.close();
        }
        catch(SQLException e)
        {
            // connection close failed.
            System.err.println(e.getMessage());
        }
    }

    /**
     * Get a list of all usernames for login
     * @return usernames
     * @throws SQLException
     */
    public static ArrayList<String> getUsernames() throws SQLException {
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);  // set timeout to 30 sec.
        ResultSet rs = statement.executeQuery("SELECT username FROM user");

        ArrayList<String> usernames = new ArrayList<>();
        while (rs.next())
            usernames.add(rs.getString("username"));

        return usernames;
    }

    /**
     * Check if user entered their correct password
     * Retrieves stored password and verifies that the entered one matches the stored hash
     * This method is not part of the User class because there isn't a user object during login yet
     * @param email
     * @param password in plain text
     * @return whether password is correct
     */
    public static boolean checkPassword(String email, String password) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT password FROM user WHERE email = ?");
        statement.setString(1, email);
        ResultSet rs = statement.executeQuery();

        rs.next();
        String storedPassword = null;
        try {
            storedPassword = rs.getString("password");
        } catch (SQLException e) {
            return false;
        }

        return BCrypt.checkpw(password, storedPassword);
    }

    /**
     * General method for inserting a single row into a database table
     * @param tableName the name of the table to which the row shall be added
     * @param parameters array consisting of pairs of column name and value
     * @throws SQLException
     * @return id of inserted row
     */
    public static int insert(String tableName, Object[][] parameters) throws SQLException {
        String query = "INSERT INTO <tableName> (<names>) VALUES (<values>)".replace("<tableName>", tableName);

        //add parameter names
        String names = "";
        String separator = ", ";
        for (int i = 0; i < parameters.length; i++)
        {
            String name = (String)parameters[i][0];
            names += name + separator;
        }
        names = names.substring(0, names.length() - separator.length()); //remove last separator
        query = query.replace("<names>", names);

        //add question marks
        String questionMarks = "?" + separator;
        questionMarks = questionMarks.repeat(parameters.length);
        questionMarks = questionMarks.substring(0, questionMarks.length() - separator.length()); //remove last separator
        query = query.replace("<values>", questionMarks);

        PreparedStatement statement = Database.connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        //insert parameter values
        for (int i = 0; i < parameters.length; i++)
        {
            Object value = parameters[i][1];
            insertValueIntoStatement(i, value, statement);
        }

        //https://stackoverflow.com/a/1915197
        int affectedRows = statement.executeUpdate();

        if (affectedRows == 0) {
            throw new SQLException("Inserting row failed, no rows affected.");
        }

        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            }
            else {
                throw new SQLException("Inserting row failed, no ID obtained.");
            }
        }
    }

    /**
     * General method for updating rows in a database table
     * @param tableName the name of the table where the row is to be found
     * @param newValues array consisting of pairs of column name and value, these values will be set on the matching rows
     * @param conditions array consisting of pairs of column name and value, rows have to match these criteria
     * @throws SQLException
     */
    public static void update(String tableName, Object[][] newValues, Object[][] conditions) throws SQLException {
        String query = "UPDATE <tableName> SET <newValues> WHERE <conditions>".replace("<tableName>", tableName);

        //add placeholders for newValues
        String separator = ", ";
        String placeHolder = "";
        for (Object[] newValue : newValues)
        {
            placeHolder += newValue[0] + " = ?" + separator;
        }
        placeHolder = placeHolder.substring(0, placeHolder.length() - separator.length()); //remove last separator
        query = query.replace("<newValues>", placeHolder);

        //add placeholders for conditions
        String conjunction = " AND ";
        String conditionsPlaceholders = "";
        for (Object[] condition : conditions)
        {
            conditionsPlaceholders += condition[0] + " = ?" + conjunction;
        }
        conditionsPlaceholders = conditionsPlaceholders.substring(0, conditionsPlaceholders.length() - conjunction.length()); //remove last conjunction
        query = query.replace("<conditions>", conditionsPlaceholders);

        PreparedStatement statement = Database.connection.prepareStatement(query);

        int i = 0; //questionMarkIndex

        //insert newValues
        for (Object[] newValue : newValues)
        {
            Object value = newValue[1];
            insertValueIntoStatement(i++, value, statement);
        }

        //insert conditions
        for (Object[] condition : conditions)
        {
            Object value = condition[1];
            insertValueIntoStatement(i++, value, statement);
        }

        statement.execute();
    }

    /**
     * General method for setting a value for a placeholder in a prepared statement
     * @param i index of the placeholder
     * @param value the value to set for the placeholder
     * @param statement the query to apply the operation to
     * @throws SQLException see the called methods
     */
    private static void insertValueIntoStatement(int i, Object value, PreparedStatement statement) throws SQLException {
        if (value instanceof String) {
            statement.setString(i+1, (String)value);
        } else if (value instanceof Boolean) {
            statement.setBoolean(i+1, (Boolean)value);
        } else if (value instanceof LocalDate) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
            String date = ((LocalDate)value).format(formatter);
            statement.setString(i+1, date);
        } else if (value instanceof LocalTime) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(timePatternAppointment);
            String time = ((LocalTime)value).format(formatter);
            statement.setString(i+1, time);
        } else {
            statement.setInt(i+1, (Integer) value);
        }
    }

    /**
     * Get all rows of users table from the database
     * For admin GUI
     * @return all users
     * @throws SQLException
     */
    public static ArrayList<User> getAllUsers() throws SQLException {
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);  // set timeout to 30 sec.
        ResultSet rs = statement.executeQuery("SELECT * FROM user");
        return loadUsersFromResultSet(rs);
    }

    /**
     * Get a user object with all user's details loaded from the database
     * @param username
     * @return
     */
    public static User getUser(String username) throws SQLException {
        String query = "SELECT * FROM user WHERE username = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, username);
        ResultSet rs = statement.executeQuery();
        return loadUsersFromResultSet(rs).get(0);
    }

    /**
     * Get a user object with all user's details loaded from the database
     * @param email
     * @return
     */
    public static User getUserFromEmail(String email) throws SQLException {
        String query = "SELECT * FROM user WHERE email = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, email);
        ResultSet rs = statement.executeQuery();
        return loadUsersFromResultSet(rs).get(0);
    }

    /**
     * Get all doctors from the database as objects of class Doctor
     * @return all doctors
     * @throws SQLException if query fails
     */
    public static ArrayList<Doctor> getDoctors() throws SQLException {
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);  // set timeout to 30 sec.
        ResultSet rs = statement.executeQuery("SELECT * FROM doctor");
        return loadDoctorsFromResultSet(rs);
    }

    /**
     * Helper method to turn result set into array of Doctor objects
     * @param rs resultSet after the query was executed
     * @return Doctor objects
     * @throws SQLException if reading an attribute fails
     */
    private static ArrayList<Doctor> loadDoctorsFromResultSet(ResultSet rs) throws SQLException {
        ArrayList<Doctor> doctors = new ArrayList<>();
        while (rs.next())
        {
            Doctor doctor = new Doctor(
                    rs.getInt("doctor_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("street"),
                    rs.getString("number"),
                    rs.getString("zip"),
                    new LatLng(rs.getDouble("latitude"), rs.getDouble("longitude"))
            );
            doctors.add(doctor);
        }
        return doctors;
    }

    public static ArrayList<HealthInformation> getHealthInformation(String email) throws SQLException {
        String query = "SELECT health_status.ICD, disease.disease_name, medication.medication_name" +
                " FROM ((((health_status INNER JOIN disease on health_status.ICD = disease.ICD)" +
                " LEFT JOIN prescription on prescription.prescription_id = health_status.prescription_id)" +
                " LEFT JOIN medication on prescription.medication_id = medication.medication_id)" +
                " INNER JOIN user on user.user_id = health_status.user_id)" +
                " WHERE user.email = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, email);
        ResultSet rs = statement.executeQuery();
        ArrayList<HealthInformation> healthList = new ArrayList<>();
        while (rs.next()) {
            HealthInformation healthInformation = new HealthInformation(
                    rs.getString("ICD"),
                    rs.getString("disease_name"),
                    rs.getString("medication_name")
            );
            healthList.add(healthInformation);
        }
        return healthList;
    }

    /**
     * Helper method to turn result set into array of User objects
     * @param rs resultSet after the query was executed
     * @return User objects
     * @throws SQLException if reading an attribute fails
     */
    private static ArrayList<User> loadUsersFromResultSet(ResultSet rs) throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        while (rs.next())
        {
            String username = rs.getString("username");
            LocalDate birthDate;
            if (username.equals("admin")) {
                birthDate = null;    //admin doesn't have a stored birthdate
            } else {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
                birthDate = LocalDate.parse(rs.getString("birthday"), formatter);
            }

            User user = new User(
                    username,
                    rs.getString("email"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("street"),
                    rs.getString("number"),
                    rs.getString("zip"),
                    birthDate,
                    rs.getString("sex"),
                    rs.getString("password"),
                    rs.getString("insurance_name"),
                    rs.getBoolean("private_insurance"),
                    false
            );
            users.add(user);
        }
        return users;
    }

    public static ArrayList<DoctorDistance> getDoctorFromDistance(String userGeoData, double distance) throws SQLException, IOException, InterruptedException, ApiException {
        String query = "SELECT * FROM doctor;";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet rs = statement.executeQuery();

        ArrayList<DoctorDistance> doctorList = new ArrayList<>();
        String address = null;

        while(rs.next()) {
            address = rs.getString("street") + " " + rs.getString("number") + ", " + rs.getString("zip");

            double resultDistance = (double)GeoDistance.getDistances(userGeoData, new String[]{address}).rows[0].elements[0].distance.inMeters/1000;

            if(resultDistance <= distance) {
                doctorList.add(new DoctorDistance(
                        resultDistance,
                        address,
                        new Doctor(
                                rs.getInt("doctor_id"),
                                rs.getString("first_name"),
                                rs.getString("last_name"),
                                rs.getString("street"),
                                rs.getString("number"),
                                rs.getString("zip"),
                                new LatLng(rs.getDouble("latitude"), rs.getDouble("longitude"))
                        )
                ));
            }
        }
        return doctorList;
    }

    public static Doctor loadDoctorFromId(int doctorId) throws SQLException {
        String query = "SELECT * FROM doctor WHERE doctor_id =" + doctorId;
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet rs = statement.executeQuery();
        double lat = Double.parseDouble(rs.getString("latitude"));
        double lng = Double.parseDouble(rs.getString("longitude"));
        LatLng location = new LatLng(lat, lng);
        Doctor doctor = new Doctor(rs.getInt("doctor_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("street"),
                rs.getString("zip"),
                rs.getString("number"),
                location);
        return doctor;
    }


    public static ArrayList<String> loadSpecialization() throws SQLException {
        String query = "SELECT * FROM category";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet rs = statement.executeQuery();
        ArrayList<String> specialization = new ArrayList<>();
        while (rs.next()) {
            specialization.add(rs.getString("category"));
        }
        return specialization;
    }

    /**
     * DEPRECATED: FoundDoctorFullController.getFreeTimeSlots() is used instead
     * @param doctor
     * @param selectedDate
     * @return
     * @throws SQLException
     */
    public static ArrayList<DoctorTimeSlot> getDoctorsFreeTimes(Doctor doctor, LocalDate selectedDate) throws SQLException {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(datePattern);
        String dateStr = selectedDate.format(dateFormatter);

        String query = "SELECT da.date, da.time, da.free" +
                " FROM doctor_appointment AS da" +
                " LEFT JOIN doctor AS d ON da.doctor_id = d.doctor_id" +
                " WHERE da.date = ? AND d.doctor_id = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, dateStr);
        statement.setInt(2, doctor.getId());
        ResultSet rs = statement.executeQuery();

        ArrayList<DoctorTimeSlot> appointments = new ArrayList<>();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(Database.timePatternAppointment);
        while(rs.next()) {
            LocalDate date = LocalDate.parse(rs.getString("date"), dateFormatter);
            LocalTime time = LocalTime.parse(rs.getString("time"), timeFormatter);
            appointments.add(new DoctorTimeSlot(date, time, rs.getBoolean("free")));
        }
        return appointments;
    }

    /**
     * Helper method to turn result set into array of appointment objects
     * @param rs resultSet after the query was executed
     * @return
     * @throws SQLException
     */
    public static ArrayList<Appointment> loadAppointmentsFromResultSet(ResultSet rs) throws SQLException {
        ArrayList<Appointment> appointments = new ArrayList<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(Database.datePattern);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(Database.timePatternAppointment);

        while (rs.next())
        {
            Appointment appointment = new Appointment(
                    false,
                    rs.getInt("id"),
                    rs.getString("user"),
                    rs.getInt("doctor_id"),
                    rs.getString("healthProblemDescription"),
                    LocalDate.parse(rs.getString("date"), dateFormatter),
                    LocalTime.parse(rs.getString("time"), timeFormatter),
                    rs.getInt("minutesBeforeReminder"),
                    rs.getInt("duration")
            );
            appointments.add(appointment);
        }
        return appointments;
    }

    /**
     * Get appointments that a doctor already has within a range of days
     * To be used to display a doctor's timetable to the patient, to find a free time for their appointment
     * @param doctor
     * @param date
     * @return doctorsAppointments
     * @throws SQLException
     */
    public static ArrayList<Appointment> getDoctorsAppointments(int doctor, LocalDate date) throws SQLException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);

        String query = "SELECT * FROM appointment WHERE doctor_id = ? AND date = ? ORDER BY date, time"; //ordering not necessary but just convenient, e.g. if it will be displayed in a list to the doctor in the future
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, doctor);
        statement.setString(2, date.format(formatter));
        ResultSet rs = statement.executeQuery();
        return loadAppointmentsFromResultSet(rs);
    }

    /**
     * Get a user's past and future appointments, ordered by appointment time (newest first)
     * To be used for the patient to see their appointments.
     * This method is not part of the User class because the content is so similar to DB.getDoctorsAppointments() and hence shall be next to it
     * @return usersAppointments
     */
    public static ArrayList<Appointment> getUsersAppointments(String username) throws SQLException {
        String query = "SELECT * FROM appointment WHERE user = ? ORDER BY date, time"; //ordering for display as list
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, username);
        ResultSet rs = statement.executeQuery();
        return loadAppointmentsFromResultSet(rs);
    }
}
