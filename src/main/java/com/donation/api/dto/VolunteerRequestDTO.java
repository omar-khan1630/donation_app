package com.donation.api.dto;

import lombok.Data;

@Data
public class VolunteerRequestDTO {
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private String aadharNumber;
    private Boolean isVerifiedByOrg;
    private Boolean policeVerificationStatus;
    private Integer organizationId;  // This will map to the Organization
}
