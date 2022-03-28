package com.example.servingwebcontent.smtp;

import org.apache.tomcat.util.json.JSONParser;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileReader;
import java.util.LinkedHashMap;
import java.util.Properties;

public class SendMail {

    private Session session;
    String username;

    public SendMail() {
        try {
            //load login creds
            JSONParser jsonParser = new JSONParser(new FileReader("src/main/java/com/example/servingwebcontent/smtp/login.json"));
            LinkedHashMap<String, Object> json = jsonParser.object();
            username = json.get("user").toString();
            String password = json.get("pass").toString();
            String host = "smtp.gmail.com";
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", "587");

            session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                        }
                    });
        } catch (Exception e) {
            System.out.println("Error initializing SMTP");
            e.printStackTrace();
        }

    }

    public boolean sendEmail(String to, String subject, String body) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);
            Transport.send(message);
            System.out.println("Sent message successfully");
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return false;
    }
}
