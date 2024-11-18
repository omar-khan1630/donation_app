package com.donation.api.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "needy")
public class Needy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer needyId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "volunteer_Id", nullable = false)
//    private Volunteer volunteer;

    @ManyToOne // Assuming a Many-to-One relationship with Volunteer
    @JoinColumn(name = "volunteer_id", nullable = false) // Set nullable to true if it's optional
    private Volunteer volunteer;
    
    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, unique = true, length = 20)
    private String samagraId;

    @Column(nullable = false, unique = true, length = 12)
    private String aadharNumber;

    @Column(nullable = false, unique = true, length = 255)
    private String email;  // Email field

    @Column(nullable = false, unique = true, length = 20)
    private String bankAccountNumber;
    
    @Column(nullable = false, length = 11) // IFSC code should be of standard length
    private String ifscCode; // Add IFSC code field

    @Column(nullable = false)
    private Boolean isVerified = false;

    @Column(nullable = false)
    private Boolean isDeleted = false;  // Soft delete flag

    @OneToMany(mappedBy = "needy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Donation> donations = new ArrayList<>();
}
