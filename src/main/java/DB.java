import org.mindrot.jbcrypt.BCrypt;

import java.io.File;
import java.sql.*;
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
        finally
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
        adminInsert.setString(1, BCrypt.hashpw(initialAdminPassword, BCrypt.gensalt()));
        adminInsert.execute();
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
}
