package com.donation.api.controller;

import com.donation.api.dto.VolunteerRequestDTO;
import com.donation.api.dto.VolunteerResponseDTO;
import com.donation.api.response.MessageResponse;
import com.donation.api.service.VolunteerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/volunteers")
public class VolunteerController {

    @Autowired
    private VolunteerService volunteerService;

    // Only organization can add a volunteer
    @PostMapping("/add")
    @PreAuthorize("hasRole('ORGANIZATION')")
    public ResponseEntity<?> addVolunteer(@RequestBody VolunteerRequestDTO volunteerRequestDTO) {
        MessageResponse responseDTO = volunteerService.createVolunteer(volunteerRequestDTO.getOrganizationId(), volunteerRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }

 // Update volunteer by email using path variable
    @PutMapping("/update/{email}")
    @PreAuthorize("hasRole('ORGANIZATION')") // Only Organization can update volunteer details
    public ResponseEntity<?> updateVolunteerByEmail(@PathVariable String email, @RequestBody VolunteerRequestDTO volunteerRequestDTO) {
        MessageResponse responseDTO = volunteerService.updateVolunteerByEmail(email, volunteerRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    // Soft Delete Volunteer
    @DeleteMapping("/{volunteerId}")
    public ResponseEntity<?> deleteVolunteer(@PathVariable Integer volunteerId) {
        volunteerService.softDeleteVolunteer(volunteerId);
        return ResponseEntity.ok(new MessageResponse("Volunteer soft-deleted successfully"));
    }

    // Get all volunteers
    @GetMapping
    public ResponseEntity<List<VolunteerResponseDTO>> getAllVolunteers() {
        List<VolunteerResponseDTO> responseDTOs = volunteerService.getAllVolunteers();
        return ResponseEntity.ok(responseDTOs);
    }

    // Get Volunteer by ID
    @GetMapping("/{volunteerId}")
    public ResponseEntity<VolunteerResponseDTO> getVolunteerById(@PathVariable Integer volunteerId) {
        Optional<VolunteerResponseDTO> responseDTO = volunteerService.getVolunteerById(volunteerId);
        return responseDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Get Volunteers by Organization ID
    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<List<VolunteerResponseDTO>> getVolunteersByOrganization(@PathVariable Integer organizationId) {
        List<VolunteerResponseDTO> responseDTOs = volunteerService.getVolunteersByOrganizationId(organizationId);
        return ResponseEntity.ok(responseDTOs);
    }
}
