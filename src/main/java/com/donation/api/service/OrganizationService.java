package com.donation.api.service;

import com.donation.api.dto.OrganizationRequestDTO;
import com.donation.api.dto.OrganizationResponseDTO;
import com.donation.api.response.MessageResponse;

import java.util.List;
import java.util.Optional;

public interface OrganizationService {

    // Create a new organization
    MessageResponse createOrganization(OrganizationRequestDTO organizationRequest);

    // Update an existing organization
    MessageResponse updateOrganization(String email, OrganizationRequestDTO organizationRequest);

    // Soft delete an organization (set deleted flag to true)
    MessageResponse softDeleteOrganization(Integer id);

    // Get an organization by its ID
    Optional<OrganizationResponseDTO> getOrganizationById(Integer id);

    // Get all non-deleted organizations
    List<OrganizationResponseDTO> getAllOrganizations();
}
