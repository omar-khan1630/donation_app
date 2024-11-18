package com.donation.api.service;

import java.util.List;

import com.donation.api.dto.DonorDTO;

public interface DonorService {
    DonorDTO createDonor(DonorDTO donorDTO);
    DonorDTO updateDonorByEmail(String email, DonorDTO donorDTO);
    void softDeleteDonor(Integer id);
    DonorDTO getDonorById(Integer id);
    List<DonorDTO> getAllDonors();
}
