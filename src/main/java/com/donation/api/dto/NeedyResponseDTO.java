package com.donation.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NeedyResponseDTO {

    private Integer needyId;
    private String name;
    private String samagraId;
    private String aadharNumber;
    private String bankAccountNumber;
    private String ifscCode;
    private Boolean isVerified;
    private String email;  // Email field
    
    private Integer volunteerId;  // Only include volunteerId, not the whole object
}
