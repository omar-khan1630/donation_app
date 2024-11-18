package com.donation.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.donation.api.entity.Organization;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Integer> {
//	boolean existsByEmail(String email); 
    // Define the method to find an organization by email
    Optional<Organization> findByEmail(String email);

    // Check if an organization exists by email
    Boolean existsByEmail(String email);

}
