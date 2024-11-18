package com.donation.api.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.donation.api.dao.IRoleRepository;
import com.donation.api.dao.UserRepository;
import com.donation.api.entity.RoleEntity;
import com.donation.api.entity.UserEntity;
import com.donation.api.request.LoginRequest;
import com.donation.api.request.SignupRequest;
import com.donation.api.response.JwtResponse;
import com.donation.api.response.MessageResponse;
import com.donation.api.security.utll.JwtTokenUtill;
import com.donation.api.service.impl.UserDetailsImpl;
import com.donation.api.utill.ERole;


/**
 * @author TA Admin
 *
 * 
 */

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	IRoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtTokenUtill jwtTokenUtill;

//	@PostConstruct
//	public void init() {
//		// Initialize default roles
//		if (roleRepository.findAll().isEmpty()) {
//			try {
//				List<RoleEntity> roleEntities = new ArrayList<>();
//				roleEntities.add(new RoleEntity(ERole.ROLE_ADMIN));
//				roleEntities.add(new RoleEntity(ERole.ROLE_ORGANIZATION));
//				roleEntities.add(new RoleEntity(ERole.ROLE_VOLUNTEER));
//				roleEntities.add(new RoleEntity(ERole.ROLE_DONOR));
//				roleEntities.add(new RoleEntity(ERole.ROLE_NEEDY));
//
//				List<RoleEntity> savedRoles = roleRepository.saveAll(roleEntities);
//
//				if (savedRoles.isEmpty()) {
//					throw new RuntimeException("Failed to initialize default roles.");
//				}
//			} catch (DataAccessException e) {
//				throw new RuntimeException("An error occurred while initializing default roles: " + e.getMessage());
//			}
//		}
//
//		if (userRepository.findByUsername("admin").isEmpty()) {
//			// Create the admin user
//			UserEntity adminUser = new UserEntity("admin", "admin@example.com", encoder.encode("123456"));
//
//			// Assign the "admin" role to the admin user
//			RoleEntity adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
//					.orElseThrow(() -> new RuntimeException("Admin role not found"));
//			adminUser.getRoles().add(adminRole);
//
//			userRepository.save(adminUser);
//		}
//	}
	@PostConstruct
	public void init() {
	    // Initialize default roles if they don't exist
	    if (roleRepository.findAll().isEmpty()) {
	        List<RoleEntity> roleEntities = new ArrayList<>();
	        roleEntities.add(new RoleEntity(ERole.ROLE_ADMIN));
	        roleEntities.add(new RoleEntity(ERole.ROLE_ORGANIZATION));
	        roleEntities.add(new RoleEntity(ERole.ROLE_VOLUNTEER));
	        roleEntities.add(new RoleEntity(ERole.ROLE_DONOR));
	        roleEntities.add(new RoleEntity(ERole.ROLE_NEEDY));
	        
	        roleRepository.saveAll(roleEntities);
	    }

	    // Create the admin user if it doesn't exist
	    if (userRepository.findByUsername("admin").isEmpty()) {
	        UserEntity adminUser = new UserEntity("admin", "admin@example.com", encoder.encode("123456"));
	        
	        RoleEntity adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
	                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
	        adminUser.getRoles().add(adminRole);  // Ensure roles are properly added
	        
	        userRepository.save(adminUser);
	    }
	}

	@PostMapping("/v1/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) throws IOException {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		JwtResponse jwt = jwtTokenUtill.generateJwtToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		Set<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.toSet());

		if (!roles.equals(loginRequest.getRole())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse("You don't have access"));
		}

		jwt.setUsername(userDetails.getUsername());
		jwt.setEmail(userDetails.getEmail());

		return ResponseEntity.ok(jwt);
	}

	 @PostMapping("/v1/verify-otp")
	    public ResponseEntity<?> verifyOtpAndSetPassword(@RequestParam String email, @RequestParam String otp, @RequestParam String newPassword) {
	        // Find user by email
	        UserEntity user = userRepository.findByEmail(email);
	        if (user == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Error: User not found."));
	        }

	        // Check if OTP matches
	        if (!user.getOtp().equals(otp)) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Error: Invalid OTP."));
	        }

	        // Set new password
	        user.setPassword(encoder.encode(newPassword));
	        user.setOtp(null); // Clear OTP after use
	        userRepository.save(user);

	        return ResponseEntity.ok(new MessageResponse("Password set successfully! You can now log in."));
	    }

	@PostMapping("/v1/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
	    // Check if the username already exists
	    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
	        return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken! Please sign in."));
	    }

	    // Check if the email already exists
	    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
	        return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use! Please sign in."));
	    }

	    // Create new user's account
	    UserEntity user = new UserEntity(signUpRequest.getUsername(), signUpRequest.getEmail(),
	            encoder.encode(signUpRequest.getPassword()));

	    Set<String> strRoles = signUpRequest.getRole();
	    Set<RoleEntity> roles = new HashSet<>();

	    // Assign roles based on the role request
	    if (strRoles == null) {
	        RoleEntity userRole = roleRepository.findByName(ERole.ROLE_NEEDY)
	                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
	        roles.add(userRole);
	    } else {
	        strRoles.forEach(role -> {
	            switch (role) {
	                case "admin":
	                    RoleEntity adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
	                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
	                    roles.add(adminRole);
	                    break;
	                case "organization":
	                    RoleEntity orgRole = roleRepository.findByName(ERole.ROLE_ORGANIZATION)
	                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
	                    roles.add(orgRole);
	                    break;
	                case "volunteer":
	                    RoleEntity volunteerRole = roleRepository.findByName(ERole.ROLE_VOLUNTEER)
	                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
	                    roles.add(volunteerRole);
	                    break;
	                case "donor":
	                    RoleEntity donorRole = roleRepository.findByName(ERole.ROLE_DONOR)
	                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
	                    roles.add(donorRole);
	                    break;
	                default:
	                    RoleEntity needyRole = roleRepository.findByName(ERole.ROLE_NEEDY)
	                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
	                    roles.add(needyRole);
	            }
	        });
	    }

	    user.setRoles(roles);
	    userRepository.save(user);

	    return ResponseEntity.ok(new MessageResponse("User registered successfully! You can now sign in."));
	}

	}
//@PostMapping("/v1/signin")
//public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
//
//	Authentication authentication = authenticationManager.authenticate(
//			new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
//
//	SecurityContextHolder.getContext().setAuthentication(authentication);
//	JwtResponse jwt = jwtTokenUtill.generateJwtToken(authentication);
//
//	UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();
//	Set<String> roles = userDetailsImpl.getAuthorities().stream().map(GrantedAuthority::getAuthority)
//			.collect(Collectors.toSet());
//	if(!roles.equals(loginRequest.getRole())) {
//		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse("You don't have access"));
//	}
//
//	return ResponseEntity.ok((jwt));
//@PostMapping("/v1/signin")
//public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) throws IOException {
//
//	Authentication authentication = authenticationManager.authenticate(
//			new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
//
//	SecurityContextHolder.getContext().setAuthentication(authentication);
//	JwtResponse jwt = jwtTokenUtill.generateJwtToken(authentication);
//
//	UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
//
//	Set<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
//			.collect(Collectors.toSet());
//
//	if (!roles.equals(loginRequest.getRole())) {
//		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse("You don't have access"));
//	}
//
//	jwt.setUsername(userDetails.getUsername());
//	jwt.setEmail(userDetails.getEmail());
//
//	return ResponseEntity.ok(jwt);
//}
//@PostMapping("/v1/signin")
//public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
//
//    // Authenticate the user based on username and password
//    Authentication authentication = authenticationManager.authenticate(
//            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
//
//    // Set authentication in the security context
//    SecurityContextHolder.getContext().setAuthentication(authentication);
//
//    // Generate JWT token
//    JwtResponse jwt = jwtTokenUtill.generateJwtToken(authentication);
//
//    // Extract user details (including roles)
//    UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();
//
//    // Fetch roles from the UserDetails implementation (as GrantedAuthority)
//    List<String> userRoles = userDetailsImpl.getAuthorities().stream()
//            .map(GrantedAuthority::getAuthority)
//            .collect(Collectors.toList());
//
//    // Handle potential null role in loginRequest safely
//    String requiredRole = (loginRequest.getRole() != null) 
//            ? "ROLE_" + loginRequest.getRole()//.trim().toUpperCase() 
//            : null;
//
//    // Debugging: Log the roles for better troubleshooting
//    System.out.println("User Roles: " + userRoles);
//    System.out.println("Required Role: " + requiredRole);
//
//    // Check if the requiredRole is valid and the user has the required role
//    if (requiredRole == null || !userRoles.contains(requiredRole)) {
//        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse("Error: You don't have access to this role."));
//    }
//
//    // If user has the required role, return JWT
//    return ResponseEntity.ok(jwt);
//}

