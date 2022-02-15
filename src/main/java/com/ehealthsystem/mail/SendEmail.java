package com.ehealthsystem.mail;

import com.ehealthsystem.pdf.CreatePDF;
import com.ehealthsystem.registration.RegistrationValidationController;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Random;

public class SendEmail {


    /**
     * Prepare an email message
     * @param session the session to use for authentication
     * @param myAccount sender email address
     * @param recipient recipient email address
     * @param subject email subject
     * @param textContent email text
     * @param withAttachment whether the message shall contain a PDF file containing the patient's information as attachment
     * @return the prepared email message
     */
    public static Message prepareMessage(Session session, String myAccount, String recipient, String subject, String textContent, boolean withAttachment) throws MessagingException, IOException, SQLException {
        Message message = new MimeMessage(session);

        message.setFrom(new InternetAddress(myAccount, "E-Health System"));
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

        if (withAttachment) {
            //add attachment to email
            Path temp = Files.createTempFile("export", ".pdf");

            String absolutePath = temp.toString();
            CreatePDF.create_Pdf(absolutePath, true);
            messageBodyPart = new MimeBodyPart();
            FileDataSource source = new FileDataSource(absolutePath);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName("Patient_Health_Information.pdf");
            multipart.addBodyPart(messageBodyPart);
        }

        message.setContent(multipart);

        return message;
    }

    public static void sendMail(String recipient, String subject, String textContent, boolean attach) throws MessagingException, IOException, SQLException {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        String myAccount = "EMAIL_ADDRESS";
        String myPassword = "PASSWORD";
        //String myPassword = ResourceReader.getResourceString("Password.txt");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myAccount, myPassword);
            }
        });

        // Message-Objekt erzeugen
        Message message = prepareMessage(session, myAccount, recipient, subject, textContent, attach);

        Transport.send(message); // E-Mail senden!
    }

    public static void validateEmail(String recipient) throws MessagingException, IOException, SQLException {
        final int CODE_LENGTH = 6;

        int minimum = (int) Math.pow(10, CODE_LENGTH - 1); // minimum value with 2 digits is 10 (10^1)
        int maximum = (int) Math.pow(10, CODE_LENGTH) - 1; // maximum value with 2 digits is 99 (10^2 - 1)
        Random random = new Random();
        int code =  minimum + random.nextInt((maximum - minimum) + 1);

        String validation = "Your confirmation code is %s.\n".formatted(code) +
                "\n" +
                "Details:\n" +
                "An account was registered with this email address on the ehealth system. If this was not you, you can safely ignore this email, your email address will not be stored.";
        SendEmail.sendMail(recipient,"Validation code", validation,false);
        RegistrationValidationController.setValidation(String.valueOf(code));
    }
}
