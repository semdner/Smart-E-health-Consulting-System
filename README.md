<div align="center">
  <h3 align="center">Smart E-health Consulting System</h3>
  <p align="center">
    A simple software to search, make and manage medical appointments.
  </p>
</div>


## About The Project

### Built With

* [JavaFX](https://openjfx.io/)
* [SQLite](https://github.com/xerial/sqlite-jdbc)
* [google-maps](https://github.com/googlemaps/google-maps-services-java)
* [JavaMail](https://javaee.github.io/javamail/)
* [jBCrypt](https://mindrot.org/projects/jBCrypt/)
* [iText7](https://github.com/itext/itext7)

## Information

### API-Key

- The API key for Google Maps has been removed for security reasons
- It can be set under `src/main/java/com/ehealthsystem/map/Context.java` in line 13
- Replace `"API_KEY"` with your API key

### Email account

> Disclaimer: As this was only a university project, the login data was stored unencrypted. This is not recommended. Use ResourceReader to store the hashed password in a file and load and decrypt it at the appropriate place

- The credentials for the email account have been removed for security reasons
- It can be set under `src/main/java/com/ehealthsystem/map/SendEmail.java` in line 76 (and 77)
- Note the disclaimer and use the commented out line 78 for your password instead of line 77

