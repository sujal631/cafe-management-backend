package com.cafe.cafemanagementsystem.utils;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailUtils {

    @Autowired
    private JavaMailSender mailSender;

    // email to all admins when one admin changes the status of a user
    public void sendSimpleMessage(String to, String subject, String text, List<String> list) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(new InternetAddress("cafe-management-system@donot.reply.com", "Cafe Management System"));
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            if (list != null && list.size() > 0) {
                helper.setCc(getCCArray(list));
            }

            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String[] getCCArray(List<String> ccList) {
        String[] cc = new String[ccList.size()];
        for (int i = 0; i < ccList.size(); i++) {
            cc[i] = ccList.get(i);
        }
        return cc;
    }

    // email for forgot password
    public void forgotPasswordMail(String to, String subject, String password) throws MessagingException {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(new InternetAddress("cafe-management-system@donot.reply.com", "Cafe Management System"));
            helper.setTo(to);
            helper.setSubject(subject);
            String htmlMsg = "<p>Dear User,</p>" +
                    "<p>We received a request to recover your login details for the Cafe Management System. Below are your credentials:</p>"
                    +
                    "<p><b>Email:</b> " + to + "</p>" +
                    "<p><b>Password:</b> " + password + "</p>" +
                    "<p>If you did not request this, please ignore this email or contact our support team immediately. For security reasons, we recommend changing your password after logging in.</p>"
                    +
                    "<p><a href=\"http://localhost:4200/\">Click here to login</a></p>" +
                    "<p>Warm regards,</p>" +
                    "<p>The Cafe Management System Team</p>";
            message.setContent(htmlMsg, "text/html");
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
