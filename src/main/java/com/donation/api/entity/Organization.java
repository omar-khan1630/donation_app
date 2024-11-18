package com.donation.api.entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "organizations")
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer organizationId;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, unique = true, length = 255)
    @Email
    private String email;

    @Column(nullable = false, length = 15)
    private String phoneNumber;

    @Column(length = 255)
    private String address;

    @Column(nullable = false)
    private Boolean deleted = false;  // Soft delete field

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Volunteer> volunteers = new ArrayList<>();
}
