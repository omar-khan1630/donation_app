package com.donation.api.repository;

import com.donation.api.entity.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VolunteerRepository extends JpaRepository<Volunteer, Integer> {
    
    // Define the method to find volunteers by organization ID
    List<Volunteer> findByOrganizationOrganizationId(Integer organizationId);
    Optional<Volunteer> findByEmail(String email);
}
