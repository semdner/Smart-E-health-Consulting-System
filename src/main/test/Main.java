import java.io.File;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        initDB();

        //Main code here

        DB.close();
    }

    /**
     * Set up database
     * @throws SQLException
     */
    private static void initDB() throws SQLException {
        if (new File(DB.fileName).isFile()) //DB file exists
            DB.init();
        else
            DB.createDB(initialAdminPassword());
    }

    /**
     * Ask the user for an initial admin password when creating the database
     * TODO To be replaced with GUI dialog
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
