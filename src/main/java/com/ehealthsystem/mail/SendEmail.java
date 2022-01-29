package com.ehealthsystem.mail;

import com.ehealthsystem.registration.RegistrationController;
import com.ehealthsystem.tools.ResourceReader;

import javafx.event.ActionEvent;
import javafx.event.Event;
import java.util.Properties;
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
import javax.sql.DataSource;

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
        String myPassword = ResourceReader.getResourceString("password.txt");

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

    public void validateEmail(String recipient) throws MessagingException {
        int[] a = new int[8]; //Größe natürlich beliebig
        int oberGrenze = 10; //kannst wählen wie du willst

        for(int i = 0; i<a.length; i++) {
            a[i] = (int)(Math.random()*oberGrenze); //casten nicht vergessen!
        }
        String validation = a.toString();
        SendEmail.sendMail(recipient,"Validation Email", validation);
        // Hier ein neues Fenster aufmachen und Validierungscode abfragen
        //TODO 
        if(validation == validation){
            
        }
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
