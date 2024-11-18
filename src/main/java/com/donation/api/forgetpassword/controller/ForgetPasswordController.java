package com.donation.api.forgetpassword.controller;


import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.donation.api.forgetpassword.entity.ResetPasswordRequest;
import com.donation.api.forgetpassword.service.OTPService;
import com.donation.api.response.MessageResponse;


@RestController
@RequestMapping("/api/auth/")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ForgetPasswordController {

	@Autowired
	private OTPService otpService;

	@PostMapping("/forgot-password")
	public ResponseEntity<?> forgotPassword(@RequestParam String email) {
		try {
			otpService.createAndSendOTP(email);
			return ResponseEntity.ok(new MessageResponse("Your OTP sent to your email successfuly."));
		} catch (MessagingException e) {
			return ResponseEntity.status(500).body(new MessageResponse("Error sending OTP email"));
		}
	}

	@PostMapping("/reset-password")
	public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
		boolean result = otpService.resetPassword(resetPasswordRequest);
		if (result) {
			return ResponseEntity.ok(new MessageResponse("your Password reset successful"));
		} else {
			return ResponseEntity.badRequest().body(new MessageResponse("Invalid OTP or OTP expired"));
		}
	}
}
