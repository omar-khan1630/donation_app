package com.donation.api.test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.donation.api.dto.DonorDTO;
import com.donation.api.entity.Donor;
import com.donation.api.entity.RoleEntity;
import com.donation.api.entity.UserEntity;
import com.donation.api.exception.ResourceNotFoundException;
import com.donation.api.repository.DonorRepository;
import com.donation.api.dao.UserRepository;
import com.donation.api.dao.IRoleRepository;
import com.donation.api.service.impl.DonorServiceImpl;
import com.donation.api.utill.ERole;

@ExtendWith(MockitoExtension.class)
public class DonorServiceTest {

    @InjectMocks
    private DonorServiceImpl donorService;  // The service we're testing

    @Mock
    private DonorRepository donorRepository;  // Mock repository to simulate database interactions

    @Mock
    private UserRepository userRepository;

    @Mock
    private IRoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private DonorDTO donorDTO;
    private Donor donor;

    @BeforeEach
    public void setUp() {
        // Initialize sample data for Donor and DonorDTO
        donor = new Donor();
        donor.setDonorId(1);
        donor.setName("John Doe");
        donor.setEmail("john.doe@example.com");
        donor.setPhoneNumber("1234567890");
        donor.setAddress("123 Main St");
        
        donorDTO = new DonorDTO();
        donorDTO.setName("John Doe");
        donorDTO.setEmail("john.doe@example.com");
        donorDTO.setPhoneNumber("1234567890");
        donorDTO.setAddress("123 Main St");
    }

    @Test
    public void testCreateDonor() {
        // Set up mock behaviors
        when(donorRepository.save(any(Donor.class))).thenReturn(donor);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        
        RoleEntity donorRole = new RoleEntity();
        donorRole.setName(ERole.ROLE_DONOR);
        
        when(roleRepository.findByName(ERole.ROLE_DONOR)).thenReturn(Optional.of(donorRole));

        // Execute the service method
        DonorDTO createdDonorDTO = donorService.createDonor(donorDTO);

        // Assert and verify behavior
        assertEquals(donorDTO.getName(), createdDonorDTO.getName());
        assertEquals(donorDTO.getEmail(), createdDonorDTO.getEmail());
        verify(donorRepository, times(1)).save(any(Donor.class));
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    public void testUpdateDonorByEmail_ExistingDonor() {
        // Mock finding donor by email
        when(donorRepository.findByEmail(donor.getEmail())).thenReturn(Optional.of(donor));
        when(userRepository.findByEmail(donor.getEmail())).thenReturn(new UserEntity());
        when(donorRepository.save(any(Donor.class))).thenReturn(donor);

        // Execute the update
        DonorDTO updatedDonorDTO = donorService.updateDonorByEmail(donor.getEmail(), donorDTO);

        // Assertions
        assertEquals(donorDTO.getName(), updatedDonorDTO.getName());
        assertEquals(donorDTO.getEmail(), updatedDonorDTO.getEmail());
        verify(donorRepository, times(1)).save(any(Donor.class));
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    public void testUpdateDonorByEmail_NonExistingDonor() {
        // Mock behavior for non-existing donor
        when(donorRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        // Expect ResourceNotFoundException when donor is not found
        assertThrows(ResourceNotFoundException.class, () -> donorService.updateDonorByEmail("nonexistent@example.com", donorDTO));
    }

    @Test
    public void testSoftDeleteDonor_ExistingDonor() {
        // Mock behavior for finding donor by id
        when(donorRepository.findById(1)).thenReturn(Optional.of(donor));

        // Execute soft delete
        donorService.softDeleteDonor(1);

        // Verify that the donor was saved with the deleted flag set to true
        assertEquals(true, donor.isDeleted());
        verify(donorRepository, times(1)).save(donor);
    }

    @Test
    public void testSoftDeleteDonor_NonExistingDonor() {
        // Mock behavior for non-existing donor
        when(donorRepository.findById(2)).thenReturn(Optional.empty());

        // Expect ResourceNotFoundException when donor is not found
        assertThrows(ResourceNotFoundException.class, () -> donorService.softDeleteDonor(2));
    }

    @Test
    public void testGetDonorById_ExistingDonor() {
        // Mock behavior for finding donor by id
        when(donorRepository.findById(1)).thenReturn(Optional.of(donor));

        // Execute get donor by id
        DonorDTO foundDonorDTO = donorService.getDonorById(1);

        // Assertions
        assertEquals(donor.getName(), foundDonorDTO.getName());
        assertEquals(donor.getEmail(), foundDonorDTO.getEmail());
        verify(donorRepository, times(1)).findById(1);
    }

    @Test
    public void testGetAllDonors() {
        // Prepare a list of donors
        List<Donor> donors = new ArrayList<>();
        donors.add(donor);
        when(donorRepository.findAll()).thenReturn(donors);

        // Execute the service method
        List<DonorDTO> donorDTOList = donorService.getAllDonors();

        // Assertions
        assertEquals(1, donorDTOList.size());
        assertEquals(donor.getName(), donorDTOList.get(0).getName());
        verify(donorRepository, times(1)).findAll();
    }
}
