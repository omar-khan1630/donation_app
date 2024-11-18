package com.donation.api.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "needy_details")
public class NeedyDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer detailId;

    @Column(name = "detail_type", nullable = false)
    private String detailType;

    @Column(name = "purpose_description", nullable = false)
    private String purposeDescription;

    @Column(name = "requested_amount")
    private Double reqAmount = 0.0;  // Default value to avoid null

    @Column(name = "remaining_amount")
    private Double remainingAmount = 0.0;  // Default value
    
    @Column(name = "email", nullable = false, unique = true)
    private String email;  // Corrected email column mapping

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted = false;  // Default value for soft delete flag
}
