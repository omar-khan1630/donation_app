package com.donation.api.forgetpassword.entity;

import lombok.Data;

@Data
public class ResetPasswordRequest {

	private String email;
	private String otp;
	private String newPassword;
}
