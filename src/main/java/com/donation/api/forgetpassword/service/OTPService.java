package com.donation.api.forgetpassword.service;

import javax.mail.MessagingException;
//
//import com.cms.api.forget.password.entity.ResetPasswordRequest;

import com.donation.api.forgetpassword.entity.ResetPasswordRequest;

public interface OTPService {

	public void sendOTPEmail(String email, String otp) throws MessagingException ;
	public void createAndSendOTP(String email) throws MessagingException;
	public boolean resetPassword(ResetPasswordRequest resetPasswordRequest) ;
}
