package com.donation.api.forgetpassword.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.donation.api.entity.UserEntity;
import com.donation.api.forgetpassword.entity.OtpEntity;


public interface OtpRepository extends JpaRepository<OtpEntity, Long> {

	OtpEntity findByUser(UserEntity user);
}
