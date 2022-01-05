import java.sql.SQLException;

public class Appointment {
    private int id, doctor, healthProblem, timestamp, minutesBeforeReminder, duration;
    private String user, healthProblemDescription;

    /**
     * Creates a new appointment object representing a row in the database
     * @param id
     * @param user
     * @param doctor
     * @param healthProblem
     * @param healthProblemDescription
     * @param timestamp
     * @param minutesBeforeReminder
     * @param duration
     */
    public Appointment(boolean insertIntoDb, int id, String user, int doctor, int healthProblem, String healthProblemDescription, int timestamp, int minutesBeforeReminder, int duration) throws SQLException {
        this.id = id;
        this.user = user;
        this.doctor = doctor;
        this.healthProblem = healthProblem;
        this.healthProblemDescription = healthProblemDescription;
        this.timestamp = timestamp;
        this.minutesBeforeReminder = minutesBeforeReminder;
        this.duration = duration;
        if (insertIntoDb)
            insertIntoDb();
    }

    /**
     * Inserts this appointment into the database.
     * Only to be used if a new appointment is created.
     * @throws SQLException
     */
    private void insertIntoDb() throws SQLException {
        Object[][] parameters = {
                {"id", id},
                {"user", user},
                {"doctor", doctor},
                {"healthProblem", healthProblem},
                {"healthProblemDescription", healthProblemDescription},
                {"timestamp", timestamp},
                {"minutesBeforeReminder", minutesBeforeReminder},
                {"duration", duration},
        };
        DB.insert("appointments", parameters);
    }

    public int getId() {
        return id;
    }

    public int getDoctor() {
        return doctor;
    }

    public int getHealthProblem() {
        return healthProblem;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public int getMinutesBeforeReminder() {
        return minutesBeforeReminder;
    }

    public int getDuration() {
        return duration;
    }

    public String getUser() {
        return user;
    }

    public String getHealthProblemDescription() {
        return healthProblemDescription;
    }

    /**
     * Updates the entry for this appointment in the database
     * @param newValues
     * @throws SQLException
     */
    private void update(Object[][] newValues) throws SQLException {
        DB.update(
                "appointments",
                newValues,
                new Object[][]{{"id", id}}
        );
    }
}
