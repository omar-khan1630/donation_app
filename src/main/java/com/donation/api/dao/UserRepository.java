package com.donation.api.dao;
/**
 * 
 */
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.donation.api.entity.UserEntity;



/**
 * @author TA Admin
 *
 * 
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

	Optional<UserEntity> findByUsername(String username);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);
	
	UserEntity findByEmail(String email);
	
	

}
