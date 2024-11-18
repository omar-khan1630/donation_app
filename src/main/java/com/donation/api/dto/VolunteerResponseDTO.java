package com.donation.api.dto;

import lombok.Data;

@Data
public class VolunteerResponseDTO {
    private Integer volunteerId;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private String aadharNumber;
    private Boolean isVerifiedByOrg;
    private Boolean policeVerificationStatus;
    private String organizationName;  // Optional, name of the organization
}
