package com.TheMFG.backend.service;

import com.TheMFG.backend.exception.EmailFaildToSendException;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.util.Properties;

@Service
public class MailService {
    private final Gmail gmail;

    @Autowired
    public MailService(Gmail gmail){
        this.gmail = gmail;
    }


    public void sendMail(String toAddress, String subject, String content) throws EmailFaildToSendException{
        Properties properties = new Properties();
        Session session = Session.getInstance(properties,null);
        MimeMessage email = new MimeMessage(session);

        try{
            email.setFrom(new InternetAddress("themfg.63@gmail.com"));
            email.addRecipient(javax.mail.Message.RecipientType.TO,new InternetAddress(toAddress));
            email.setSubject(subject);
            email.setText(content);

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            email.writeTo(buffer);

            byte[] rawMessageBytes = buffer.toByteArray();
            String encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes);

            Message message = new Message();
            message.setRaw(encodedEmail);
            message = gmail.users().messages().send("me",message).execute();
        }catch (Exception e){
            throw new EmailFaildToSendException();
        }
    }
}
