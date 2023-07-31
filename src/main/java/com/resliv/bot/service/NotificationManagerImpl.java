package com.resliv.bot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.*;
import javax.mail.internet.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@Slf4j
public class NotificationManagerImpl implements NotificationManager {

    private static final String SENDER_EMAIL = "vin76423@gmail.com";
    private static final String RECIPIENT_EMAIL = "vilchinskioleg@gmail.com";

    private static final String SUBJECT = "Congratulation! You won!";
    private static final String TEXT = "Hello, my dearest Anastasiia! You won my game.. So that I'd love to provide you with that (attached) gift. I love you :)";
    private static final String TEXT_CONTENT_TYPE = "text/plain"; // text/plain ; application/json
    private static final String FILE_PATH = "/Users/aleh.vilchynski/Downloads/abce.jpg";


    @Resource
    private JavaMailSender emailSender;


    @Override
    public void sendCongratulationEmail() {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            message.setFrom(new InternetAddress(SENDER_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(RECIPIENT_EMAIL));
            message.setSubject(SUBJECT);

            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(TEXT);

            Path path = Paths.get(FILE_PATH);
            byte[] content = Files.readAllBytes(path);

            InternetHeaders headers = new InternetHeaders();
            headers.addHeader("Content-Type", "image/jpeg");
            headers.addHeader("Mime-Type", "image/jpeg");
            headers.addHeader("Content-Transfer-Encoding", "utf-8");

            MimeBodyPart attachmentPart = new MimeBodyPart(headers, content);
            attachmentPart.setFileName("filename.jpeg");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(attachmentPart);

            message.setContent(multipart);
            emailSender.send(message);
        } catch (Exception ex) {
            log.error("Unexpected error during send email for user= {}", RECIPIENT_EMAIL, ex);
            throw new RuntimeException(ex);
        }
    }
}