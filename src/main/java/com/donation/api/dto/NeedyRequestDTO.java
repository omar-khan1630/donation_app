package com.donation.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NeedyRequestDTO {
    private String name;
    private String email;
    private String aadharNumber;
    private String samagraId;
    private String bankAccountNumber;
    private String ifscCode;
    private boolean isVerified; // Adjust based on your requirements
    private Long volunteerId; // Add this field
    private String password;  // Add this line

    // Getters and setters...
}
