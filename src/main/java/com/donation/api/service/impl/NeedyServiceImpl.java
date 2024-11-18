package com.donation.api.service.impl;

import com.donation.api.dao.IRoleRepository;
import com.donation.api.dao.UserRepository;
import com.donation.api.dto.NeedyRequestDTO;
import com.donation.api.dto.NeedyResponseDTO;
import com.donation.api.entity.Needy;
import com.donation.api.entity.RoleEntity;
import com.donation.api.entity.UserEntity;
import com.donation.api.entity.Volunteer;
import com.donation.api.exception.ResourceNotFoundException;
import com.donation.api.forgetpassword.mail.EmailService;
import com.donation.api.forgetpassword.service.OTPStorageService;
import com.donation.api.repository.NeedyRepository;
import com.donation.api.repository.VolunteerRepository;
import com.donation.api.response.MessageResponse;
import com.donation.api.service.NeedyService;
import com.donation.api.utility.OTPUtility;
import com.donation.api.utill.ERole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NeedyServiceImpl implements NeedyService {

    @Autowired
    private NeedyRepository needyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private VolunteerRepository volunteerRepository;
    

    @Autowired
    private OTPStorageService otpStorageService;

    @Autowired
    private EmailService emailService;
    
    @Override
    public NeedyResponseDTO createNeedy(NeedyRequestDTO needyRequest) {
        // Check for existing email
        if (needyRepository.findByEmail(needyRequest.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Needy with email " + needyRequest.getEmail() + " already exists.");
        }

        // Create and save Needy entity
        Needy needy = new Needy();
        needy.setName(needyRequest.getName());
        needy.setEmail(needyRequest.getEmail());
        needy.setAadharNumber(needyRequest.getAadharNumber());
        needy.setSamagraId(needyRequest.getSamagraId());
        needy.setBankAccountNumber(needyRequest.getBankAccountNumber());
        needy.setIfscCode(needyRequest.getIfscCode()); // Set IFSC code
        needy.setIsVerified(needyRequest.isVerified());

        // Retrieve Volunteer entity based on volunteerId
        Volunteer volunteer = volunteerRepository.findById(needyRequest.getVolunteerId().intValue()) // Use the injected volunteerRepository
            .orElseThrow(() -> new IllegalArgumentException("Volunteer not found with id " + needyRequest.getVolunteerId()));
        needy.setVolunteer(volunteer); // Set the volunteer

        needyRepository.save(needy);

        // Create UserEntity for the Needy
        createUserForNeedy(needy, needyRequest.getPassword()); // Assume password is passed in the request

        return mapToResponseDTO(needy); // Map to NeedyResponseDTO and return
    }
//    @Override
//    public MessageResponse createUserForNeedy(Needy needy, String password) {
//        UserEntity userEntity = new UserEntity();
//        userEntity.setUsername(needy.getEmail());
//        userEntity.setEmail(needy.getEmail());
//        userEntity.setPassword(passwordEncoder.encode(password)); // Encrypt the password
//
//        // Set the role to NEEDY
//        RoleEntity needyRole = roleRepository.findByName(ERole.ROLE_NEEDY)
//                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//        userEntity.getRoles().add(needyRole);
//
//        userRepository.save(userEntity);
//        return new MessageResponse("User created for the needy successfully.");
//    }
    @Override
    public MessageResponse createUserForNeedy(Needy needy, String password) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(needy.getEmail());
        userEntity.setEmail(needy.getEmail());
        userEntity.setPassword(passwordEncoder.encode(password)); // Encrypt the password

        // Find the role NEEDY and add it to the user's roles
        RoleEntity needyRole = roleRepository.findByName(ERole.ROLE_NEEDY)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        userEntity.getRoles().add(needyRole);  // Make sure roles are added correctly to the user
        // Generate OTP and expiration time
        String otp = OTPUtility.generateOTP();
        LocalDateTime expirationTime = OTPUtility.getExpirationTime(10); // 10 minutes

        // Store OTP in the OTP storage service and also in the user entity
        otpStorageService.storeOTP(needy.getEmail(), otp, expirationTime);
        userEntity.setOtp(otp);

        // Save the user entity
        userRepository.save(userEntity);

        // Send the welcome email with username and OTP
        emailService.sendWelcomeEmail(needy.getEmail(), needy.getEmail(), otp);

        return new MessageResponse("Volunteer created successfully and welcome email sent with OTP.");
    }

    @Override
    public MessageResponse updateNeedyByEmail(String email, NeedyRequestDTO needyRequest) {
        Needy needy = needyRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Needy", "Email", email));

        needy.setName(needyRequest.getName());
        needy.setAadharNumber(needyRequest.getAadharNumber());
        needy.setSamagraId(needyRequest.getSamagraId());
        needy.setBankAccountNumber(needyRequest.getBankAccountNumber());
        needy.setIfscCode(needyRequest.getIfscCode());
        //needy.setEmail(needyRequest.getEmail());

        needyRepository.save(needy);
        return new MessageResponse("Needy updated successfully.");
    }

    @Override
    public MessageResponse softDeleteNeedy(Integer id) {
        Needy needy = needyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Needy", "ID", id));

        needy.setIsDeleted(true);
        needyRepository.save(needy);
        return new MessageResponse("Needy soft-deleted successfully.");
    }

    @Override
    public Optional<NeedyResponseDTO> getNeedyById(Integer id) {
        Needy needy = needyRepository.findById(id)
                .filter(n -> !n.getIsDeleted()) // Ensure it is not soft deleted
                .orElseThrow(() -> new ResourceNotFoundException("Needy", "ID", id));

        return Optional.of(mapToResponseDTO(needy));
    }

    @Override
    public List<NeedyResponseDTO> getAllNeedies() {
        return needyRepository.findAllByIsDeletedFalse().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    private NeedyResponseDTO mapToResponseDTO(Needy needy) {
        NeedyResponseDTO responseDTO = new NeedyResponseDTO();
        responseDTO.setNeedyId(needy.getNeedyId());
        responseDTO.setName(needy.getName());
        responseDTO.setEmail(needy.getEmail());
        responseDTO.setAadharNumber(needy.getAadharNumber());
        responseDTO.setSamagraId(needy.getSamagraId());
        responseDTO.setBankAccountNumber(needy.getBankAccountNumber());
        responseDTO.setIfscCode(needy.getIfscCode());
        responseDTO.setIsVerified(needy.getIsVerified());
        responseDTO.setVolunteerId(needy.getVolunteer() != null ? needy.getVolunteer().getVolunteerId() : null);  // Ensure volunteerId is set
        return responseDTO;
    }
}
