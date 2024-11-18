package com.donation.api.controller;

import com.donation.api.dto.OrganizationRequestDTO;
import com.donation.api.dto.OrganizationResponseDTO;
import com.donation.api.response.MessageResponse;
import com.donation.api.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/organizations")
@Validated
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    @PostMapping
    public ResponseEntity<MessageResponse> createOrganization(@Valid @RequestBody OrganizationRequestDTO organizationRequest) {
        return ResponseEntity.ok(organizationService.createOrganization(organizationRequest));
    }

 // Update an existing organization by email
    @PutMapping("/{email}")
    public ResponseEntity<MessageResponse> updateOrganization(@PathVariable String email, @Valid @RequestBody OrganizationRequestDTO organizationRequest) {
        return ResponseEntity.ok(organizationService.updateOrganization(email, organizationRequest));
    }

    // Soft delete an organization by ID
    @PutMapping("/delete/{id}")
    public ResponseEntity<MessageResponse> softDeleteOrganization(@PathVariable Integer id) {
        return ResponseEntity.ok(organizationService.softDeleteOrganization(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationResponseDTO> getOrganizationById(@PathVariable Integer id) {
        Optional<OrganizationResponseDTO> organizationOptional = organizationService.getOrganizationById(id);

        // Handle Optional and return appropriate ResponseEntity
        if (organizationOptional.isPresent()) {
            return ResponseEntity.ok(organizationOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<OrganizationResponseDTO>> getAllOrganizations() {
        return ResponseEntity.ok(organizationService.getAllOrganizations());
    }
}
