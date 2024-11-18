package com.donation.api.service;

import java.util.List;
import java.util.Optional;

import com.donation.api.dto.NeedyRequestDTO;
import com.donation.api.dto.NeedyResponseDTO;
import com.donation.api.entity.Needy;
import com.donation.api.response.MessageResponse;

public interface NeedyService {

    /**
     * Creates a new Needy entity.
     * 
     * @param needyRequest the DTO containing the needy details.
     * @return the created NeedyResponseDTO.
     */
	   public NeedyResponseDTO createNeedy(NeedyRequestDTO needyRequest) ;

    /**
     * Updates an existing Needy entity by its ID.
     * 
     * @param id the ID of the Needy entity to update.
     * @param needyRequest the DTO containing updated needy details.
     * @return the updated NeedyResponseDTO.
     */
	public MessageResponse updateNeedyByEmail(String email, NeedyRequestDTO needyRequest);

    /**
     * Soft deletes a Needy entity by its ID.
     * 
     * @param id the ID of the Needy entity to soft delete.
     */
	 public MessageResponse softDeleteNeedy(Integer id);

    /**
     * Retrieves a Needy entity by its ID.
     * 
     * @param id the ID of the Needy entity to retrieve.
     * @return an Optional containing the NeedyResponseDTO if found.
     */
    Optional<NeedyResponseDTO> getNeedyById(Integer id);

    /**
     * Retrieves all verified Needy entities.
     * 
     * @return a list of NeedyResponseDTO representing all verified needy entities.
     */
    List<NeedyResponseDTO> getAllNeedies();

    /**
     * Retrieves all Needy entities associated with a specific volunteer ID.
     * 
     * @param volunteerId the ID of the volunteer.
     * @return a list of NeedyResponseDTO associated with the specified volunteer ID.
     */
  //  List<NeedyResponseDTO> getNeediesByVolunteerId(Integer volunteerId);
    public MessageResponse createUserForNeedy(Needy needy, String password) ;

//    if (needyRequestDTO.getPassword() != null) {
//        needy.setPassword(passwordEncoder.encode(needyRequestDTO.getPassword()));
//    }
}
