package com.donation.api.dto;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NeedyDetailsDTO {

    private Integer detailId;

    @NotNull(message = "Detail type cannot be null")
    private String detailType;

    @NotNull(message = "Purpose description cannot be null")
    private String purposeDescription;

    private Double reqAmount;

    @NotNull(message = "Remaining amount cannot be null")
    private Double remainingAmount;

    @NotNull(message = "Email cannot be null")
    private String email;  // Email field added to DTO

    private boolean deleted;  // Soft delete flag added
}
