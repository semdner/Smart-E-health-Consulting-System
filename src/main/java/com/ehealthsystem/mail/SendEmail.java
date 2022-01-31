package com.ehealthsystem.mail;

import com.ehealthsystem.registration.RegistrationController;
import com.ehealthsystem.registration.RegistrationValidationController;
import com.ehealthsystem.tools.ResourceReader;

import com.ehealthsystem.tools.SceneSwitch;
import javafx.event.Event;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Random;
import javax.activation.FileDataSource;
import javax.activation.DataHandler;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendEmail {

    public static Message prepareMessage(Session session, String myAccount, String recipient, String subject, String textContent) throws MessagingException {
        Message message = new MimeMessage(session);

        message.setFrom(new InternetAddress(myAccount));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        message.setSubject(subject);

        // Multipart-Message ("Wrapper") erstellen
        Multipart multipart = new MimeMultipart();
        // Body-Part setzen:
        BodyPart messageBodyPart = new MimeBodyPart();
        // Textteil des Body-Parts
        messageBodyPart.setText(textContent);
        // Body-Part dem Multipart-Wrapper hinzufügen
        multipart.addBodyPart(messageBodyPart);
        // Message fertigstellen, indem sie mit dem Multipart-Content ausgestattet wird

        /*
        messageBodyPart = new MimeBodyPart();
        String filename = "file.txt";
        DataSource source = new FileDataSource(filename);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(filename);
        multipart.addBodyPart(messageBodyPart);
        */

        message.setContent(multipart);

        return message;
    }

    public static void sendMail(String recipient, String subject, String textContent) throws MessagingException {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        String myAccount = "testemailverteiler2@gmail.com";
        String myPassword = "FraUAS123";
        //String myPassword = ResourceReader.getResourceString("FraUAS123");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myAccount, myPassword);
            }
        });

        // Message-Objekt erzeugen und senden!
        Message message = prepareMessage(session, myAccount, recipient, subject, textContent);
        Transport.send(message); // E-Mail senden!
    }

    public static void validateEmail(String recipient) throws MessagingException {
        int[] a = new int[8]; //Größe natürlich beliebig
        int oberGrenze = 10; //kannst wählen wie du willst

        int minimum = (int) Math.pow(10, 8 - 1); // minimum value with 2 digits is 10 (10^1)
        int maximum = (int) Math.pow(10, 8) - 1; // maximum value with 2 digits is 99 (10^2 - 1)
        Random random = new Random();
        int code =  minimum + random.nextInt((maximum - minimum) + 1);

        String validation = String.valueOf(code);
        SendEmail.sendMail(recipient,"Validation Email", validation);
        RegistrationValidationController.setValidation(validation);
        /*
        //TODO Hier ein neues Fenster aufmachen und Validierungscode abfragen
        public void openValidation{
            if(validation == validation){
                private void loadPrimaryWindow(Event event) throws IOException, SQLException {
                    com.ehealthsystem.tools.Session.loginUser(emailTextField.getText());
                    SceneSwitch.switchToCentered(event, "primary/primary-view.fxml", "E-Health System");
                }
                }else{
                //TODO Erneut abfragen oder Email neu senden???
            }
        }*/
    }
        // Prepare Message with Attachment to send pdf of Health Informations
    public static Message prepareMessageWithAttachment(Session session, String myAccount, String recipient, String subject, String textContent, String filename ) throws MessagingException {
        Message message = new MimeMessage(session);

        message.setFrom(new InternetAddress(myAccount));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        message.setSubject(subject);

        // Multipart-Message ("Wrapper") erstellen
        Multipart multipart = new MimeMultipart();
        // Body-Part setzen:
        BodyPart messageBodyPart = new MimeBodyPart();
        // Textteil des Body-Parts
        messageBodyPart.setText(textContent);
        // Body-Part dem Multipart-Wrapper hinzufügen
        multipart.addBodyPart(messageBodyPart);
        // Message fertigstellen, indem sie mit dem Multipart-Content ausgestattet wird


        messageBodyPart = new MimeBodyPart();
        FileDataSource source = new FileDataSource(filename);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(filename);
        multipart.addBodyPart(messageBodyPart);


        message.setContent(multipart);

        return message;
    }
}
