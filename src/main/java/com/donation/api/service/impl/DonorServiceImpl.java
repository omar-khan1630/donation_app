package com.donation.api.service.impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.donation.api.dao.IRoleRepository;
import com.donation.api.dao.UserRepository;
import com.donation.api.dto.DonorDTO;
import com.donation.api.entity.Donor;
import com.donation.api.entity.RoleEntity;
import com.donation.api.entity.UserEntity;
import com.donation.api.exception.ResourceNotFoundException;
import com.donation.api.repository.DonorRepository;
import com.donation.api.service.DonorService;
import com.donation.api.utill.ERole;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DonorServiceImpl implements DonorService {

    @Autowired
    private DonorRepository donorRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder; // For encoding passwords
    
    @Autowired
    private UserRepository userRepository; 
    
    @Autowired
    private IRoleRepository roleRepository;

    @Override
    public DonorDTO createDonor(DonorDTO donorDTO) {
        // Create and save Donor entity
        Donor donor = new Donor();
        donor.setName(donorDTO.getName());
        donor.setEmail(donorDTO.getEmail());
        donor.setPhoneNumber(donorDTO.getPhoneNumber());
        donor.setAddress(donorDTO.getAddress());
        
        // Save the donor to the database
        Donor savedDonor = donorRepository.save(donor);

        // Create and save User entity with the same data
        UserEntity user = new UserEntity();
        user.setUsername(donorDTO.getName());
        user.setEmail(donorDTO.getEmail());
        user.setPassword(passwordEncoder.encode("defaultPassword123")); // Set a default password or generate a secure one
        
        Set<RoleEntity> roles = new HashSet<>();
        RoleEntity donorRole = roleRepository.findByName(ERole.ROLE_DONOR)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(donorRole);
        user.setRoles(roles);
       // userRepository.save(userEntity);
        // Save the user entity
        userRepository.save(user);

        // Return the saved donor entity as DTO
        return convertToDTO(savedDonor);
    }

    public DonorDTO updateDonorByEmail(String email, DonorDTO donorDTO) {
        // Find the donor by email
        Donor donor = donorRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Donor", "email", email));

        // Update donor details
        donor.setName(donorDTO.getName());
        donor.setPhoneNumber(donorDTO.getPhoneNumber());
        donor.setAddress(donorDTO.getAddress());
        
        // Save updated donor
        Donor updatedDonor = donorRepository.save(donor);

        // Find the corresponding UserEntity and update it
        UserEntity user = userRepository.findByEmail(email);
             //   .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        
        // Update the user details based on the donor changes
        user.setUsername(donorDTO.getName());  // Sync donor name with username
        user.setEmail(donorDTO.getEmail());    // Sync donor email with user email
        
        // Save updated user entity
        userRepository.save(user);

        // Return the updated donor as DTO
        return convertToDTO(updatedDonor);
    }


    @Override
    public void softDeleteDonor(Integer id) {
        Donor donor = donorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donor", "id", id));
        donor.setDeleted(true);
        donorRepository.save(donor);
    }

    @Override
    public DonorDTO getDonorById(Integer id) {
        Donor donor = donorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donor", "id", id));
        return convertToDTO(donor);
    }

    @Override
    public List<DonorDTO> getAllDonors() {
        return donorRepository.findAll().stream()
                .filter(donor -> !donor.isDeleted())
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private DonorDTO convertToDTO(Donor donor) {
        DonorDTO dto = new DonorDTO();
        dto.setDonorId(donor.getDonorId());
        dto.setName(donor.getName());
        dto.setEmail(donor.getEmail());
        dto.setPhoneNumber(donor.getPhoneNumber());
        dto.setAddress(donor.getAddress());
        return dto;
    }
}
