package com.donation.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.donation.api.entity.Needy;
import java.util.Optional;

@Repository
public interface NeedyRepository extends JpaRepository<Needy, Integer> {
  
    // Fetch only the verified needy entries
    List<Needy> findAllByIsVerifiedTrue();
    
    // Custom method to fetch all non-deleted needy entries
    List<Needy> findAllByIsDeletedFalse();
    
    // Fetch by volunteerId if needed
    List<Needy> findByVolunteerVolunteerId(Integer volunteerId);

    // Add method to find needy by email
    Optional<Needy> findByEmail(String email); // This will allow you to fetch needy by email
}
