package com.donation.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.donation.api.entity.NeedyDetails;
@Repository
public interface NeedyDetailsRepository extends JpaRepository<NeedyDetails, Integer> {
    // Find NeedyDetails by needy email
    Optional<NeedyDetails> findByEmail(String email);
}
