package com.donation.api.service;

import com.donation.api.dto.DonationDTO;

import java.util.List;

public interface DonationService {
    DonationDTO createDonation(DonationDTO donationDTO);
    DonationDTO updateDonation(Integer id, DonationDTO donationDTO);
    void softDeleteDonation(Integer id);
    DonationDTO getDonationById(Integer id);
    List<DonationDTO> getAllDonations();
}
