package com.donation.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DonationDTO {
    private Integer donationId;

    @NotNull(message = "Donor ID is required")
    private Integer donorId;

    @NotNull(message = "Needy ID is required")
    private Integer needyId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Double amount;

    @NotNull(message = "Transaction ID is required")
    @Size(min = 1, max = 100, message = "Transaction ID must be between 1 and 100 characters")
    private String transactionId;

    @NotNull(message = "Transfer success status is required")
    private Boolean isTransferSuccessful;
}
