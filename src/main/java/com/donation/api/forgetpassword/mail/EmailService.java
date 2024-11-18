package com.donation.api.forgetpassword.mail;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

//
//import javax.mail.MessagingException;
//import javax.mail.internet.MimeMessage;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.stereotype.Service;

@Service
public class EmailService {
	

	@Autowired
	private JavaMailSender mailSender;
	

    public void sendWelcomeEmail(String toEmail, String username, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Welcome to the App!");

        String mailContent = String.format(
            "Hello %s,\n\nWelcome to the app!\nYour username: %s\nYour OTP: %s\nThis OTP is valid for 10 minutes.",
            username, toEmail, otp
        );

        message.setText(mailContent);
        mailSender.send(message);
    }
	
	public void sendEmail(String to, String subject,String text) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message,true);
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setFrom("Neural Bridge AI <mfurqan9988@gmail.com>");
		helper.setText(text,true);
		mailSender.send(message);
	}
}
