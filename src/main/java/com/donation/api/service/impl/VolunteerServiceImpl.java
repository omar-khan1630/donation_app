package com.donation.api.service.impl;

import com.donation.api.dao.IRoleRepository;
import com.donation.api.dao.UserRepository;
import com.donation.api.dto.VolunteerRequestDTO;
import com.donation.api.dto.VolunteerResponseDTO;
import com.donation.api.entity.Organization;
import com.donation.api.entity.RoleEntity;
import com.donation.api.entity.Volunteer;
import com.donation.api.exception.ResourceNotFoundException;
import com.donation.api.forgetpassword.mail.EmailService;
import com.donation.api.forgetpassword.service.OTPStorageService;
import com.donation.api.repository.OrganizationRepository;
import com.donation.api.repository.VolunteerRepository;
import com.donation.api.response.MessageResponse;
import com.donation.api.service.VolunteerService;
import com.donation.api.utility.OTPUtility;
import com.donation.api.utill.ERole;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import com.donation.api.entity.UserEntity; // Import UserEntity
@Service
public class VolunteerServiceImpl implements VolunteerService {

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private UserRepository userRepository; // Add user repository

    @Autowired
    private PasswordEncoder passwordEncoder; // For encoding passwords
    
    @Autowired
    private IRoleRepository roleRepository;
    

    @Autowired
    private OTPStorageService otpStorageService;

    @Autowired
    private EmailService emailService;
    
    @Override
    public MessageResponse createVolunteer(Integer organizationId, VolunteerRequestDTO volunteerRequest) {
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization", "ID", organizationId));

        Volunteer volunteer = new Volunteer();
        volunteer.setName(volunteerRequest.getName());
        volunteer.setEmail(volunteerRequest.getEmail());
        volunteer.setPhoneNumber(volunteerRequest.getPhoneNumber());
        volunteer.setAddress(volunteerRequest.getAddress());
        volunteer.setAadharNumber(volunteerRequest.getAadharNumber());
        volunteer.setIsVerifiedByOrg(volunteerRequest.getIsVerifiedByOrg());
        volunteer.setPoliceVerificationStatus(volunteerRequest.getPoliceVerificationStatus());
        volunteer.setOrganization(organization);
        volunteer.setDeleted(false);

        // Save the volunteer first
        volunteerRepository.save(volunteer);

        // Create a user for the volunteer
        createUserForVolunteer(volunteer);

        return new MessageResponse("Volunteer created successfully and associated with Organization ID " + organizationId);
    }

    @Override
    public MessageResponse createUserForVolunteer(Volunteer volunteer) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(volunteer.getEmail());
        userEntity.setEmail(volunteer.getEmail());
        userEntity.setPassword(passwordEncoder.encode("defaultPassword123")); // You may want to encrypt the password

        // Assign the VOLUNTEER role
        Set<RoleEntity> roles = new HashSet<>();
        RoleEntity volunteerRole = roleRepository.findByName(ERole.ROLE_VOLUNTEER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(volunteerRole);
        userEntity.setRoles(roles);
        
        // Generate OTP and expiration time
        String otp = OTPUtility.generateOTP();
        LocalDateTime expirationTime = OTPUtility.getExpirationTime(10); // 10 minutes

        // Store OTP in the OTP storage service and also in the user entity
        otpStorageService.storeOTP(volunteer.getEmail(), otp, expirationTime);
        userEntity.setOtp(otp);

        // Save the user entity
        userRepository.save(userEntity);

        // Send the welcome email with username and OTP
        emailService.sendWelcomeEmail(volunteer.getEmail(), volunteer.getEmail(), otp);

        return new MessageResponse("Volunteer created successfully and welcome email sent with OTP.");
    }

    @Override
    public MessageResponse updateVolunteerByEmail(String email, VolunteerRequestDTO volunteerRequest) {
        Volunteer volunteer = volunteerRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Volunteer", "Email", email));

        volunteer.setName(volunteerRequest.getName());
        volunteer.setPhoneNumber(volunteerRequest.getPhoneNumber());
        volunteer.setAddress(volunteerRequest.getAddress());
        volunteer.setAadharNumber(volunteerRequest.getAadharNumber());
        volunteer.setIsVerifiedByOrg(volunteerRequest.getIsVerifiedByOrg());
        volunteer.setPoliceVerificationStatus(volunteerRequest.getPoliceVerificationStatus());

        volunteerRepository.save(volunteer);
        return new MessageResponse("Volunteer updated successfully.");
    }
//@Service
//public class VolunteerServiceImpl implements VolunteerService {
//
//    @Autowired
//    private VolunteerRepository volunteerRepository;
//
//    @Autowired
//    private OrganizationRepository organizationRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private IRoleRepository roleRepository;
//
//    @Autowired
//    private PasswordEncoder encoder; // To encrypt passwords
//
//    @Override
//    public MessageResponse createVolunteer(Integer organizationId, VolunteerRequestDTO volunteerRequest) {
//        Organization organization = organizationRepository.findById(organizationId)
//                .orElseThrow(() -> new ResourceNotFoundException("Organization", "ID", organizationId));
//
//        Volunteer volunteer = new Volunteer();
//        volunteer.setName(volunteerRequest.getName());
//        volunteer.setEmail(volunteerRequest.getEmail());
//        volunteer.setPhoneNumber(volunteerRequest.getPhoneNumber());
//        volunteer.setAddress(volunteerRequest.getAddress());
//        volunteer.setAadharNumber(volunteerRequest.getAadharNumber());
//        volunteer.setIsVerifiedByOrg(volunteerRequest.getIsVerifiedByOrg());
//        volunteer.setPoliceVerificationStatus(volunteerRequest.getPoliceVerificationStatus());
//        volunteer.setOrganization(organization);
//        volunteer.setDeleted(false);
//
//        // Save the volunteer first
//        volunteerRepository.save(volunteer);
//
//        // Create a user for the volunteer
//        createUserForVolunteer(volunteer);
//
//        return new MessageResponse("Volunteer created successfully and associated with Organization ID " + organizationId);
//    }
//
//    @Override
//    public MessageResponse createUserForVolunteer(Volunteer volunteer) {
//        UserEntity userEntity = new UserEntity();
//        userEntity.setUsername(volunteer.getEmail());
//        userEntity.setEmail(volunteer.getEmail());
//        userEntity.setPassword(encoder.encode(volunteer.getPhoneNumber())); // Encrypt password
//
//        // Assign the VOLUNTEER role
//        Set<RoleEntity> roles = new HashSet<>();
//        RoleEntity volunteerRole = roleRepository.findByName(ERole.ROLE_VOLUNTEER)
//                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//        roles.add(volunteerRole);
//        userEntity.setRoles(roles);
//
//        userRepository.save(userEntity);
//
//        return new MessageResponse("User created for the volunteer successfully.");
//    }
//
//    @Override
//    public MessageResponse updateVolunteerByEmail(String email, VolunteerRequestDTO volunteerRequest) {
//        Volunteer volunteer = volunteerRepository.findByEmail(email)
//                .orElseThrow(() -> new ResourceNotFoundException("Volunteer", "Email", email));
//
//        volunteer.setName(volunteerRequest.getName());
//        volunteer.setPhoneNumber(volunteerRequest.getPhoneNumber());
//        volunteer.setAddress(volunteerRequest.getAddress());
//        volunteer.setAadharNumber(volunteerRequest.getAadharNumber());
//        volunteer.setIsVerifiedByOrg(volunteerRequest.getIsVerifiedByOrg());
//        volunteer.setPoliceVerificationStatus(volunteerRequest.getPoliceVerificationStatus());
//
//        // Save the updated volunteer
//        volunteerRepository.save(volunteer);
//
//        // Update corresponding user entity
//        updateUserForVolunteer(volunteer);
//
//        return new MessageResponse("Volunteer updated successfully.");
//    }
//    public MessageResponse updateUserForVolunteer(Volunteer volunteer) {
//        // Find user by email
//        UserEntity userEntity = userRepository.findByEmail(volunteer.getEmail());
//
//        // Check if userEntity is null, if so, throw exception
//        if (userEntity == null) {
//            throw new ResourceNotFoundException("User", "Email", volunteer.getEmail());
//        }
//
//        // Update user details
//        userEntity.setUsername(volunteer.getEmail());
//        userEntity.setEmail(volunteer.getEmail());
//        userEntity.setPassword(encoder.encode(volunteer.getPhoneNumber())); // Update password
//
//        userRepository.save(userEntity);
//
//        return new MessageResponse("User details updated successfully for the volunteer.");
//    }

    @Override
    public MessageResponse softDeleteVolunteer(Integer id) {
        Volunteer volunteer = volunteerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Volunteer", "ID", id));

        volunteer.setDeleted(true);
        volunteerRepository.save(volunteer);
        return new MessageResponse("Volunteer soft-deleted successfully.");
    }

    @Override
    public Optional<VolunteerResponseDTO> getVolunteerById(Integer id) {
        Volunteer volunteer = volunteerRepository.findById(id)
                .filter(v -> !v.getDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Volunteer", "ID", id));

        return Optional.of(mapToResponseDTO(volunteer));
    }

    @Override
    public List<VolunteerResponseDTO> getVolunteersByOrganizationId(Integer organizationId) {
        return volunteerRepository.findByOrganizationOrganizationId(organizationId).stream()
                .filter(volunteer -> !volunteer.getDeleted())
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<VolunteerResponseDTO> getAllVolunteers() {
        return volunteerRepository.findAll().stream()
                .filter(volunteer -> !volunteer.getDeleted())
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    private VolunteerResponseDTO mapToResponseDTO(Volunteer volunteer) {
        VolunteerResponseDTO responseDTO = new VolunteerResponseDTO();
        responseDTO.setVolunteerId(volunteer.getVolunteerId());
        responseDTO.setName(volunteer.getName());
        responseDTO.setEmail(volunteer.getEmail());
        responseDTO.setPhoneNumber(volunteer.getPhoneNumber());
        responseDTO.setAddress(volunteer.getAddress());
        responseDTO.setAadharNumber(volunteer.getAadharNumber());
        responseDTO.setIsVerifiedByOrg(volunteer.getIsVerifiedByOrg());
        responseDTO.setPoliceVerificationStatus(volunteer.getPoliceVerificationStatus());
        responseDTO.setOrganizationName(volunteer.getOrganization().getName());
        return responseDTO;
    }
}
