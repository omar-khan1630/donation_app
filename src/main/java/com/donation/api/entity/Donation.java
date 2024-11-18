package com.donation.api.entity;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "donations")
public class Donation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer donationId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donor_id", nullable = false)
    private Donor donor;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "needy_id", nullable = false)
    private Needy needy;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private Double amount;
    
    @Column(nullable = false, unique = true, length = 100)
    private String transactionId;
    
    @Column(nullable = false)
    private Boolean isTransferSuccessful = false;

    // Soft delete field
    @Column(nullable = false)
    private Boolean isDeleted = false;
}
