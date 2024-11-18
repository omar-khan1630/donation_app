package com.donation.api.service;

import com.donation.api.dto.VolunteerRequestDTO;
import com.donation.api.dto.VolunteerResponseDTO;
import com.donation.api.entity.Volunteer;
import com.donation.api.response.MessageResponse;

import java.util.List;
import java.util.Optional;

public interface VolunteerService {

    // Create a new volunteer for a specific organization
	 
    MessageResponse createVolunteer(Integer organizationId, VolunteerRequestDTO volunteerRequest);
    
    // New method to create a user for the volunteer
    MessageResponse createUserForVolunteer(Volunteer volunteer);

    // Update an existing volunteer
    public MessageResponse updateVolunteerByEmail(String email, VolunteerRequestDTO volunteerRequest) ;

    // Soft delete a volunteer (set deleted flag to true)
    MessageResponse softDeleteVolunteer(Integer id);

    // Get a volunteer by ID
    Optional<VolunteerResponseDTO> getVolunteerById(Integer id);

    // Get all volunteers for an organization
    List<VolunteerResponseDTO> getVolunteersByOrganizationId(Integer organizationId);

    // Get all non-deleted volunteers
    List<VolunteerResponseDTO> getAllVolunteers();
}
