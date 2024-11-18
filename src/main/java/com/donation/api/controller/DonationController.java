package com.donation.api.controller;

import com.donation.api.dto.DonationDTO;
import com.donation.api.exception.ResourceNotFoundException;
import com.donation.api.service.DonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/donations")
public class DonationController {

    @Autowired
    private DonationService donationService;

    // Create a new donation
    @PostMapping
    public ResponseEntity<DonationDTO> createDonation(@Valid @RequestBody DonationDTO donationDTO) {
        DonationDTO createdDonation = donationService.createDonation(donationDTO);
        return new ResponseEntity<>(createdDonation, HttpStatus.CREATED);
    }

    // Get a donation by ID
    @GetMapping("/{id}")
    public ResponseEntity<DonationDTO> getDonationById(@PathVariable Integer id) {
        DonationDTO donationDTO = donationService.getDonationById(id);
        return ResponseEntity.ok(donationDTO);
    }

    // Update a donation by ID
    @PutMapping("/{id}")
    public ResponseEntity<DonationDTO> updateDonation(@PathVariable Integer id, @Valid @RequestBody DonationDTO donationDTO) {
        DonationDTO updatedDonation = donationService.updateDonation(id, donationDTO);
        return ResponseEntity.ok(updatedDonation);
    }

    // Get all donations
    @GetMapping
    public ResponseEntity<List<DonationDTO>> getAllDonations() {
        List<DonationDTO> donationList = donationService.getAllDonations();
        return ResponseEntity.ok(donationList);
    }

    // Soft delete a donation by ID
    @PatchMapping("/{id}/soft-delete")
    public ResponseEntity<String> softDeleteDonation(@PathVariable Integer id) {
        donationService.softDeleteDonation(id);
        return ResponseEntity.ok("Donation has been soft deleted successfully.");
    }
}
