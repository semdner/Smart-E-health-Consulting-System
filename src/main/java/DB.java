import org.mindrot.jbcrypt.BCrypt;

import java.io.File;
import java.sql.*;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

//Database
public class DB {
    static Connection connection = null;

    /**
     * To be called on application start.
     * Establishes connection to the database.
     * Creates a database if it doesn't exist yet.
     * Before creating a database, it asks for an admin password.
     */
    public static void init()
    {
        boolean isExistingDb = new File("ehealth.sqlite3").isFile(); //there isn't a database file

        String initialAdminPassword = null;
        if (!isExistingDb)
            initialAdminPassword = initialAdminPassword(); //ask before creating file!

        try
        {
            connection = DriverManager.getConnection("jdbc:sqlite:ehealth.sqlite3"); //creates file if it doesn't exist
            if (!isExistingDb)
                createDB(initialAdminPassword);
        }
        catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
    }

    /**
     * Ask for admin password and create database
     */
    private static void createDB(String initialAdminPassword) throws SQLException {
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);  // set timeout to 30 sec.

        //Create tables
        statement.execute(ResourceReader.getResourceString("createTableUsers.sql"));
        statement.execute(ResourceReader.getResourceString("createTableDoctors.sql"));
        statement.execute(ResourceReader.getResourceString("createTableAppointments.sql"));

        statement.execute(ResourceReader.getResourceString("createTableProblems.sql"));
        statement.execute(ResourceReader.getResourceString("createTableSpecializations.sql"));
        statement.execute(ResourceReader.getResourceString("createTableSuitableSpecializations.sql"));
        statement.execute(ResourceReader.getResourceString("insertIntoProblems.sql"));
        statement.execute(ResourceReader.getResourceString("insertIntoSpecializations.sql"));
        statement.execute(ResourceReader.getResourceString("insertIntoSuitableSpecializations.sql"));

        //Insert admin user
        String query = "INSERT INTO users (username, isAdmin, password) VALUES ('admin', 1, ?)";
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
     * Ask the user for an initial admin password when creating the database
     * @return desired admin password entered by the user
     */
    private static String initialAdminPassword()
    {
        System.out.println("Please enter your desired admin password: ");

        Scanner in = new Scanner(System.in);
        String password = in.nextLine();
        in.close();

        return password;
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
        ResultSet rs = statement.executeQuery("SELECT username FROM users");

        ArrayList<String> usernames = new ArrayList<>();
        while (rs.next())
            usernames.add(rs.getString("username"));

        return usernames;
    }

    /**
     * Check if user entered their correct password
     * Retrieves stored password and verifies that the entered one matches the stored hash
     * This method is not part of the User class because there isn't a user object during login yet
     * @param username
     * @param password in plain text
     * @return whether password is correct
     */
    public static boolean checkPassword(String username, String password) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT password FROM users WHERE username = ?");
        statement.setString(1, username);
        ResultSet rs = statement.executeQuery();

        rs.next();
        String storedPassword = rs.getString("password");

        return BCrypt.checkpw(password, storedPassword);
    }

    /**
     * General method for inserting a single row into a database table
     * @param tableName the name of the table to which the row shall be added
     * @param parameters array consisting of pairs of column name and value
     * @throws SQLException
     */
    public static void insert(String tableName, Object[][] parameters) throws SQLException {
        String query = "INSERT INTO <tableName> (<names>) VALUES (<values>)".replace("<tableName>", tableName);

        //add parameter names
        String names = "";
        for (int i = 0; i < parameters.length; i++)
        {
            String name = (String)parameters[i][0];
            names += name + ", ";
        }
        names = names.substring(0, names.length() - 2); //remove last ", "
        query = query.replace("<names>", names);

        //add question marks
        String questionMarks = "?, ";
        questionMarks = questionMarks.repeat(parameters.length);
        questionMarks = questionMarks.substring(0, questionMarks.length() - 2); //remove last ", "
        query = query.replace("<values>", questionMarks);

        PreparedStatement statement = DB.connection.prepareStatement(query);
        //insert parameter values
        for (int i = 0; i < parameters.length; i++)
        {
            Object value = parameters[i][1];
            if (value instanceof String)
                statement.setString(i+1, (String)value);
            else if (value instanceof Boolean)
                statement.setBoolean(i+1, (Boolean)value);
            else
                statement.setInt(i+1, (Integer)value);
        }

        statement.execute();
    }

    /**
     * Get all rows of users table from the database
     * @return all users
     * @throws SQLException
     */
    public static ArrayList<User> getAllUsersDetails() throws SQLException {
        ArrayList<User> users = new ArrayList<>();

        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);  // set timeout to 30 sec.
        ResultSet rs = statement.executeQuery("SELECT * FROM users");
        while (rs.next())
        {
            LocalDate birthDate = null;
            try
            {
                birthDate = LocalDate.of(rs.getInt("birthYear"), rs.getInt("birthMonth"), rs.getInt("birthDay"));
            }
            catch (DateTimeException e)
            {
                //admin doesn't have a stored birthdate
            }

            User user = new User(
                    rs.getString("username"),
                    false,
                    null,
                    rs.getString("firstName"),
                    rs.getString("lastName"),
                    rs.getString("mail"),
                    rs.getString("street"),
                    rs.getString("houseNo"),
                    rs.getInt("zipCode"),
                    birthDate,
                    rs.getString("preExistingConditions"),
                    rs.getString("allergies"),
                    rs.getString("pastTreatments"),
                    rs.getString("currentTreatments"),
                    rs.getString("medications"),
                    rs.getString("insurance"),
                    rs.getBoolean("privateInsurance")
            );
            users.add(user);
        }

        return users;
    }
}
