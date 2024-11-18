package com.donation.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.donation.api.dto.NeedyDetailsDTO;
import com.donation.api.response.MessageResponse;
import com.donation.api.service.NeedyDetailsService;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/needy-details")
public class NeedyDetailsController {

    @Autowired
    private NeedyDetailsService needyDetailsService;

    // Create a new NeedyDetails entry (restricted to Organization/Volunteer roles)
    @PostMapping
    public ResponseEntity<?> createNeedyDetails(@Valid @RequestBody NeedyDetailsDTO needyDetailsDTO) {
        NeedyDetailsDTO createdNeedyDetails = needyDetailsService.createNeedyDetails(needyDetailsDTO);
        return new ResponseEntity<>(createdNeedyDetails, HttpStatus.CREATED);  // 201 Created
    }

    // Update existing NeedyDetails by email
    @PutMapping("/update/{email}")
    public ResponseEntity<?> updateNeedyDetails(
            @PathVariable String email, 
            @Valid @RequestBody NeedyDetailsDTO needyDetailsDTO) {
        NeedyDetailsDTO updatedNeedyDetails = needyDetailsService.updateNeedyDetailsByEmail(email, needyDetailsDTO);
        return ResponseEntity.ok(updatedNeedyDetails);  // 200 OK
    }

    // Soft delete NeedyDetails by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> softDeleteNeedyDetails(@PathVariable Integer id) {
        needyDetailsService.softDeleteNeedyDetails(id);
        return ResponseEntity.ok(new MessageResponse("NeedyDetails successfully soft deleted."));  // 200 OK with message
    }

    // Get NeedyDetails by email
    @GetMapping("/{email}")
    public ResponseEntity<?> getNeedyDetailsByEmail(@PathVariable String email) {
        NeedyDetailsDTO needyDetails = needyDetailsService.getNeedyDetailsByEmail(email);
        return ResponseEntity.ok(needyDetails);  // 200 OK
    }

    // Get all NeedyDetails
    @GetMapping
    public ResponseEntity<?> getAllNeedyDetails() {
        List<NeedyDetailsDTO> allNeedyDetails = needyDetailsService.getAllNeedyDetails();
        return ResponseEntity.ok(allNeedyDetails);  // 200 OK
    }
}
