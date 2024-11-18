package com.donation.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DonorDTO {
    private Integer donorId;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
}
