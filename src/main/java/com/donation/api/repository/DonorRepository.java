package com.donation.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.donation.api.entity.Donor;

@Repository
public interface DonorRepository extends JpaRepository<Donor, Integer> {
    // Additional query methods if needed
	Optional<Donor> findByEmail(String email);
}
