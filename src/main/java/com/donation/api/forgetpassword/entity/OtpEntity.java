package com.donation.api.forgetpassword.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.donation.api.entity.UserEntity;

import lombok.Data;

@Entity
@Table(name = "otps")
@Data
public class OtpEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String otp;
	private LocalDateTime otpExpirationTime;
	
	@OneToOne
	@JoinColumn(name = "user_id")
	private UserEntity user;
}
