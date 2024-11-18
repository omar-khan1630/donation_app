package com.donation.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import com.donation.api.dto.NeedyDetailsDTO;
import com.donation.api.entity.NeedyDetails;
import com.donation.api.exception.ResourceNotFoundException;
import com.donation.api.repository.NeedyDetailsRepository;
import com.donation.api.service.NeedyDetailsService;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NeedyDetailsServiceImpl implements NeedyDetailsService {

    @Autowired
    private NeedyDetailsRepository needyDetailsRepository;

    @Override
    public NeedyDetailsDTO createNeedyDetails(NeedyDetailsDTO needyDetailsDTO) {
        // Check if the user has the correct role (Organization or Volunteer)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .filter(r -> r.equals("ROLE_ORGANIZATION") || r.equals("ROLE_VOLUNTEER"))
            .findFirst()
            .orElseThrow(() -> new AccessDeniedException("You do not have permission to add NeedyDetails"));

        NeedyDetails needyDetails = mapToEntity(needyDetailsDTO);
        NeedyDetails savedNeedyDetails = needyDetailsRepository.save(needyDetails);
        return mapToDTO(savedNeedyDetails);
    }

    @Override
    public NeedyDetailsDTO updateNeedyDetailsByEmail(String email, NeedyDetailsDTO needyDetailsDTO) {
        NeedyDetails existingNeedyDetails = needyDetailsRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("NeedyDetails", "email", email));

        // Update fields
        existingNeedyDetails.setDetailType(needyDetailsDTO.getDetailType());
        existingNeedyDetails.setPurposeDescription(needyDetailsDTO.getPurposeDescription());
        existingNeedyDetails.setReqAmount(needyDetailsDTO.getReqAmount() != null ? needyDetailsDTO.getReqAmount() : 0.0);
       // existingNeedyDetails.setEmail(needyDetailsDTO.getEmail());
        existingNeedyDetails.setRemainingAmount(needyDetailsDTO.getRemainingAmount() != null ? needyDetailsDTO.getRemainingAmount() : 0.0);
        

        NeedyDetails updatedNeedyDetails = needyDetailsRepository.save(existingNeedyDetails);
        return mapToDTO(updatedNeedyDetails);
    }

    @Override
    public NeedyDetailsDTO getNeedyDetailsByEmail(String email) {
        NeedyDetails needyDetails = needyDetailsRepository.findByEmail(email)
                .filter(nd -> !nd.isDeleted()) // Exclude soft-deleted records
                .orElseThrow(() -> new ResourceNotFoundException("NeedyDetails", "email", email));
        return mapToDTO(needyDetails);
    }

    @Override
    public void softDeleteNeedyDetails(Integer id) {
        NeedyDetails needyDetails = needyDetailsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("NeedyDetails", "id", id));

        // Soft delete by marking the `deleted` flag as true
        needyDetails.setDeleted(true);
        needyDetailsRepository.save(needyDetails);
    }

    @Override
    public List<NeedyDetailsDTO> getAllNeedyDetails() {
        return needyDetailsRepository.findAll().stream()
                .filter(needyDetails -> !needyDetails.isDeleted()) // Exclude soft-deleted records
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Helper methods to map between entity and DTO
    private NeedyDetailsDTO mapToDTO(NeedyDetails needyDetails) {
        if (needyDetails == null) {
            throw new IllegalArgumentException("NeedyDetails cannot be null");
        }
        return new NeedyDetailsDTO(
            needyDetails.getDetailId(),
            needyDetails.getDetailType(),
            needyDetails.getPurposeDescription(),
            needyDetails.getReqAmount(),
            needyDetails.getRemainingAmount(),
            needyDetails.getEmail(),  // Include email in the mapping
            needyDetails.isDeleted()  // Map deleted flag as well
        );
    }

    private NeedyDetails mapToEntity(NeedyDetailsDTO needyDetailsDTO) {
        NeedyDetails needyDetails = new NeedyDetails();
        needyDetails.setDetailType(needyDetailsDTO.getDetailType());
        needyDetails.setPurposeDescription(needyDetailsDTO.getPurposeDescription());
        needyDetails.setReqAmount(needyDetailsDTO.getReqAmount());
        needyDetails.setRemainingAmount(needyDetailsDTO.getRemainingAmount());
        needyDetails.setEmail(needyDetailsDTO.getEmail());  // Set email in the entity
        needyDetails.setDeleted(needyDetailsDTO.isDeleted());
        return needyDetails;
    }
}
