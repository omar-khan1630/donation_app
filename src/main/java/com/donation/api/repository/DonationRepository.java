package com.donation.api.repository;

import com.donation.api.entity.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface DonationRepository extends JpaRepository<Donation, Integer> {
    Optional<Donation> findByDonationIdAndIsDeletedFalse(Integer donationId); // Use donationId instead of id
    List<Donation> findAllByIsDeletedFalse();
}
