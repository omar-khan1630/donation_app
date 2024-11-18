package com.donation.api.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "volunteers")
public class Volunteer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer volunteerId;

    @ManyToOne
    @JoinColumn(name = "organization_id", nullable = false)
    @JsonBackReference
    private Organization organization;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false, length = 15)
    private String phoneNumber;

    @Column(length = 255)
    private String address;

    @Column(nullable = false)
    private Boolean isVerifiedByOrg = false;

    @Column(nullable = false)
    private Boolean policeVerificationStatus = false;

    @Column(nullable = false, unique = true, length = 12)
    private String aadharNumber;

    @Column(nullable = false)
    private Boolean deleted = false;  // Soft delete flag

    @Column(nullable = false)
    private Boolean isActive = true;  // New field to track active status

    @OneToMany(mappedBy = "volunteer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Needy> needyList = new ArrayList<>();
    
    // Add getter for isActive
    public Boolean getIsActive() {
        return isActive;
    }
}
