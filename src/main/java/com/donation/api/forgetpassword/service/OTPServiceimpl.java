package com.donation.api.forgetpassword.service;


import java.time.LocalDateTime;
import java.util.Random;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
//
//import com.cms.api.dao.UserRepository;
//import com.cms.api.entity.UserEntity;
//import com.cms.api.forget.password.entity.OtpEntity;
//import com.cms.api.forget.password.entity.ResetPasswordRequest;
//import com.cms.api.forget.password.mail.EmailService;
//import com.cms.api.forget.password.repository.OtpRepository;

import com.donation.api.dao.UserRepository;
import com.donation.api.entity.UserEntity;
import com.donation.api.forgetpassword.entity.OtpEntity;
import com.donation.api.forgetpassword.entity.ResetPasswordRequest;
import com.donation.api.forgetpassword.mail.EmailService;
import com.donation.api.forgetpassword.repository.OtpRepository;

@Service
public class OTPServiceimpl implements OTPService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private OtpRepository otpRepository;

	@Autowired
	private EmailService emailService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private static final int OTP_EXPIRATION_MINUTES = 10;

	public String generateOTP() {
		Random random = new Random();
		int otp = 100000 + random.nextInt(900000);
		return String.valueOf(otp);
	}

	@Override
	public void sendOTPEmail(String email, String otp) throws MessagingException {
		System.out.println("email : " + email);
		String subject = "Your OTP for Password Reset";
		String message = "Dear User,\n\n"
				+ "This email is to inform you that a one-time password (OTP) has been generated for the purpose of resetting your password.\n\n"
				+ "OTP: " + otp + "\n\n"
				+ "Please use this OTP within the next 10 minutes, as it will expire thereafter.\n\n" + "Best regards.";
		emailService.sendEmail(email, subject, message);

	}

	@Override
	public void createAndSendOTP(String email) throws MessagingException {
		UserEntity user = userRepository.findByEmail(email);
		if (user != null) {
			String otp = generateOTP();
			OtpEntity otpEntity = otpRepository.findByUser(user);
			if (otpEntity == null) {
				otpEntity = new OtpEntity();
				otpEntity.setUser(user);
			}
			otpEntity.setOtp(otp);
			otpEntity.setOtpExpirationTime(LocalDateTime.now().plusMinutes(OTP_EXPIRATION_MINUTES));
			otpRepository.save(otpEntity);
			sendOTPEmail(email, otp);
		}
	}

	@Override
	public boolean resetPassword(ResetPasswordRequest resetPasswordRequest) {
		UserEntity user = userRepository.findByEmail(resetPasswordRequest.getEmail());
		if (user != null) {
			OtpEntity otpEntity = otpRepository.findByUser(user);
			if (otpEntity != null && otpEntity.getOtp().equals(resetPasswordRequest.getOtp())
					&& otpEntity.getOtpExpirationTime().isAfter(LocalDateTime.now())) {
				user.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
				otpEntity.setOtp(null);
				otpEntity.setOtpExpirationTime(null);
				userRepository.save(user);
				otpRepository.save(otpEntity);
				return true;
			}
		}
		return false;
	}
}
