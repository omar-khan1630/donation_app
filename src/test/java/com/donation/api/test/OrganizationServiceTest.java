package com.donation.api.test;

import com.donation.api.dao.UserRepository;
import com.donation.api.dao.IRoleRepository;
import com.donation.api.dto.OrganizationRequestDTO;
import com.donation.api.dto.OrganizationResponseDTO;
import com.donation.api.entity.Organization;
import com.donation.api.entity.UserEntity;
import com.donation.api.entity.RoleEntity;
import com.donation.api.exception.ResourceNotFoundException;
import com.donation.api.forgetpassword.mail.EmailService;
import com.donation.api.forgetpassword.service.OTPStorageService;
import com.donation.api.repository.OrganizationRepository;
import com.donation.api.response.MessageResponse;
import com.donation.api.service.impl.OrganizationServiceImpl;
import com.donation.api.utility.OTPUtility;
import com.donation.api.utill.ERole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class OrganizationServiceTest {

    @InjectMocks
    private OrganizationServiceImpl organizationService;

    @Mock
    private OrganizationRepository organizationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private IRoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private OTPStorageService otpStorageService;

    @Mock
    private EmailService emailService;

    private OrganizationRequestDTO organizationRequestDTO;

    @BeforeEach
    public void setUp() {
        // Initialize test data
        organizationRequestDTO = new OrganizationRequestDTO();
        organizationRequestDTO.setName("Test Organization");
        organizationRequestDTO.setEmail("test@org.com");
        organizationRequestDTO.setPhoneNumber("1234567890");
        organizationRequestDTO.setAddress("Test Address");
        organizationRequestDTO.setUsername("testuser");
        organizationRequestDTO.setPassword("Test@123");
    }
    @Test
    public void createOrganization_ShouldCreateSuccessfully() {
        // Mock repository calls
        when(organizationRepository.existsByEmail(organizationRequestDTO.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(organizationRequestDTO.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(organizationRequestDTO.getEmail())).thenReturn(false);
        when(organizationRepository.save(any(Organization.class))).thenReturn(new Organization());
        when(roleRepository.findByName(ERole.ROLE_ORGANIZATION)).thenReturn(Optional.of(new RoleEntity()));

        // Mock the storeOTP method using doNothing() since it's void
        doNothing().when(otpStorageService).storeOTP(any(), any(), any());

        // Act
        MessageResponse response = organizationService.createOrganization(organizationRequestDTO);

        // Assertions
        assertEquals("Organization and user account created successfully, OTP sent to email.", response.getMessage());
        verify(organizationRepository, times(1)).save(any(Organization.class));
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    public void updateOrganization_ShouldUpdateSuccessfully() {
        // Mock repository calls
        Organization existingOrg = new Organization();
        existingOrg.setEmail("test@org.com");
        when(organizationRepository.findByEmail(organizationRequestDTO.getEmail()))
                .thenReturn(Optional.of(existingOrg));
        when(organizationRepository.save(any(Organization.class))).thenReturn(existingOrg);

        MessageResponse response = organizationService.updateOrganization(organizationRequestDTO.getEmail(), organizationRequestDTO);

        // Assertions
        assertEquals("Organization and user account updated successfully.", response.getMessage());
        verify(organizationRepository, times(1)).save(any(Organization.class));
    }

    @Test
    public void softDeleteOrganization_ShouldDeleteSuccessfully() {
        // Mock repository calls
        Organization existingOrg = new Organization();
        existingOrg.setDeleted(false);
        when(organizationRepository.findById(1)).thenReturn(Optional.of(existingOrg));

        MessageResponse response = organizationService.softDeleteOrganization(1);

        // Assertions
        assertEquals("Organization soft deleted successfully.", response.getMessage());
        assertTrue(existingOrg.getDeleted());
        verify(organizationRepository, times(1)).save(existingOrg);
    }

    @Test
    public void getOrganizationById_ShouldReturnOrganization() {
        // Mock repository calls
        Organization existingOrg = new Organization();
        existingOrg.setEmail("test@org.com");
        when(organizationRepository.findById(1)).thenReturn(Optional.of(existingOrg));

        Optional<OrganizationResponseDTO> response = organizationService.getOrganizationById(1);

        // Assertions
        assertTrue(response.isPresent());
        assertEquals("test@org.com", response.get().getEmail());
        verify(organizationRepository, times(1)).findById(1);
    }

    @Test
    public void getOrganizationById_ShouldThrowExceptionWhenNotFound() {
        // Mock repository calls
        when(organizationRepository.findById(1)).thenReturn(Optional.empty());

        // Test exception thrown
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                organizationService.getOrganizationById(1));

        assertEquals("Organization", exception.getResourceName());
        assertEquals("ID", exception.getFieldName());
    }
}