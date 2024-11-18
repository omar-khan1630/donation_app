package com.donation.api.service.impl;

import com.donation.api.dao.IRoleRepository;
import com.donation.api.dao.UserRepository;
import com.donation.api.dto.OrganizationRequestDTO;
import com.donation.api.dto.OrganizationResponseDTO;
import com.donation.api.entity.Organization;
import com.donation.api.entity.RoleEntity;
import com.donation.api.entity.UserEntity;
import com.donation.api.exception.ResourceNotFoundException;
import com.donation.api.forgetpassword.mail.EmailService;
import com.donation.api.forgetpassword.service.OTPStorageService;
import com.donation.api.repository.OrganizationRepository;
//import com.donation.api.repository.RoleRepository;
import com.donation.api.response.MessageResponse;
import com.donation.api.service.OrganizationService;
import com.donation.api.utility.OTPUtility;
import com.donation.api.utill.ERole;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class OrganizationServiceImpl implements OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private OTPStorageService otpStorageService;
    
    @Autowired
    private EmailService emailService;


//    @Override
//    @PreAuthorize("hasRole('ADMIN')") // Only Admin can add an organization
//    public MessageResponse createOrganization(OrganizationRequestDTO organizationRequest) {
//        // Check for a null password
//        if (organizationRequest.getPassword() == null || organizationRequest.getPassword().isEmpty()) {
//            throw new IllegalArgumentException("Password cannot be null or empty.");
//        }
//
//        // Existing checks...
//        if (organizationRepository.existsByEmail(organizationRequest.getEmail())) {
//            throw new IllegalArgumentException("Organization with email " + organizationRequest.getEmail() + " already exists.");
//        }
//
//        if (userRepository.existsByUsername(organizationRequest.getUsername())) {
//            throw new IllegalArgumentException("Username " + organizationRequest.getUsername() + " is already taken.");
//        }
//        if (userRepository.existsByEmail(organizationRequest.getEmail())) {
//            throw new IllegalArgumentException("Email " + organizationRequest.getEmail() + " is already used by another user.");
//        }
//
//        // Create and save Organization
//        Organization organization = new Organization();
//        organization.setName(organizationRequest.getName());
//        organization.setEmail(organizationRequest.getEmail());
//        organization.setPhoneNumber(organizationRequest.getPhoneNumber());
//        organization.setAddress(organizationRequest.getAddress());
//        organizationRepository.save(organization);
//
//        // Create UserEntity for the organization
//        UserEntity user = new UserEntity();
//        user.setUsername(organizationRequest.getUsername());
//        user.setEmail(organizationRequest.getEmail());
//        user.setPassword(passwordEncoder.encode(organizationRequest.getPassword())); // Encode the password
//
//
//        // Also store the OTP in the user entity
//       // user.setOtp(otp);
//        // Set the role to ORGANIZATION
//        RoleEntity organizationRole = roleRepository.findByName(ERole.ROLE_ORGANIZATION)
//                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//        user.getRoles().add(organizationRole);
//
//        // Save the user entity
//     // Generate OTP
//        String otp = OTPUtility.generateOTP();
//        LocalDateTime expirationTime = OTPUtility.getExpirationTime(10); // 10 minutes
//
//        // Store OTP and its expiration
//        otpStorageService.storeOTP(organization.getEmail(), otp, expirationTime);
//
//        // Send Welcome Email
//        emailService.sendWelcomeEmail(organization.getEmail(), organization.getEmail(), otp);
//
//        userRepository.save(user);
//
//        return new MessageResponse("Organization and user account created successfully.");
//    }


    @Override
    @PreAuthorize("hasRole('ADMIN')") // Only Admin can add an organization
    public MessageResponse createOrganization(OrganizationRequestDTO organizationRequest) {
        // Check for a null password
        if (organizationRequest.getPassword() == null || organizationRequest.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty.");
        }

        // Existing checks...
        if (organizationRepository.existsByEmail(organizationRequest.getEmail())) {
            throw new IllegalArgumentException("Organization with email " + organizationRequest.getEmail() + " already exists.");
        }

        if (userRepository.existsByUsername(organizationRequest.getUsername())) {
            throw new IllegalArgumentException("Username " + organizationRequest.getUsername() + " is already taken.");
        }
        if (userRepository.existsByEmail(organizationRequest.getEmail())) {
            throw new IllegalArgumentException("Email " + organizationRequest.getEmail() + " is already used by another user.");
        }

        // Create and save Organization
        Organization organization = new Organization();
        organization.setName(organizationRequest.getName());
        organization.setEmail(organizationRequest.getEmail());
        organization.setPhoneNumber(organizationRequest.getPhoneNumber());
        organization.setAddress(organizationRequest.getAddress());
        organizationRepository.save(organization);

        // Create UserEntity for the organization
        UserEntity user = new UserEntity();
        user.setUsername(organizationRequest.getUsername());
        user.setEmail(organizationRequest.getEmail());
        user.setPassword(passwordEncoder.encode(organizationRequest.getPassword())); // Encode the password

        // Generate OTP
        String otp = OTPUtility.generateOTP();
        LocalDateTime expirationTime = OTPUtility.getExpirationTime(10); // 10 minutes

        // Store OTP and its expiration in the OTP storage service
        otpStorageService.storeOTP(organization.getEmail(), otp, expirationTime);

        // Also store the OTP in the user entity
        user.setOtp(otp);

        // Set the role to ORGANIZATION
        RoleEntity organizationRole = roleRepository.findByName(ERole.ROLE_ORGANIZATION)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        user.getRoles().add(organizationRole);

        // Save the user entity with the OTP
        userRepository.save(user);

        // Send Welcome Email with OTP
        emailService.sendWelcomeEmail(organization.getEmail(), organization.getEmail(), otp);

        return new MessageResponse("Organization and user account created successfully, OTP sent to email.");
    }

    @Override
    public MessageResponse updateOrganization(String email, OrganizationRequestDTO organizationRequest) {
        // Fetch the organization by email, throwing exception if not found
    	Organization organization = organizationRepository.findByEmail(organizationRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Organization", "Email", organizationRequest.getEmail()));
        // Update the organization's fields
        organization.setName(organizationRequest.getName());
        organization.setPhoneNumber(organizationRequest.getPhoneNumber());
        organization.setAddress(organizationRequest.getAddress());

        // Save the updated organization
        organizationRepository.save(organization);

        // Update user information
//        UserEntity user = userRepository.findByEmail(organizationRequest.getEmail());
//        user.setUsername(organizationRequest.getUsername());
//        user.setPassword(passwordEncoder.encode(organizationRequest.getPassword()));

        // Save updated user information
//        userRepository.save(user);

        return new MessageResponse("Organization and user account updated successfully.");
    }

    @Override
    public MessageResponse softDeleteOrganization(Integer id) {
        // Fetch the organization by ID
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organization", "ID", id));

        // Set the soft delete flag
        organization.setDeleted(true);
        organizationRepository.save(organization);
        return new MessageResponse("Organization soft deleted successfully.");
    }

    @Override
    public Optional<OrganizationResponseDTO> getOrganizationById(Integer id) {
        // Fetch organization by ID, only if not deleted
        Organization organization = organizationRepository.findById(id)
                .filter(org -> !org.getDeleted()) // Ensure it is not soft deleted
                .orElseThrow(() -> new ResourceNotFoundException("Organization", "ID", id));

        // Return the DTO in an Optional wrapper
        return Optional.of(mapToResponseDTO(organization));
    }

    @Override
    public List<OrganizationResponseDTO> getAllOrganizations() {
        // Fetch all organizations that are not soft deleted
        return organizationRepository.findAll().stream()
                .filter(organization -> !organization.getDeleted())
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Helper method to map Organization entity to OrganizationResponseDTO
    private OrganizationResponseDTO mapToResponseDTO(Organization organization) {
        return new OrganizationResponseDTO(
                organization.getOrganizationId(),
                organization.getName(),
                organization.getEmail(),
                organization.getPhoneNumber(),
                organization.getAddress(),
                organization.getDeleted()
        );
    }
}
