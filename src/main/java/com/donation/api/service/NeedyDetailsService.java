package com.donation.api.service;

import com.donation.api.dto.NeedyDetailsDTO;
import java.util.List;

public interface NeedyDetailsService {
    NeedyDetailsDTO createNeedyDetails(NeedyDetailsDTO needyDetailsDTO);
    NeedyDetailsDTO updateNeedyDetailsByEmail(String email, NeedyDetailsDTO needyDetailsDTO);  // Updated to use email
    void softDeleteNeedyDetails(Integer id);
    NeedyDetailsDTO getNeedyDetailsByEmail(String email);  // Added a method to fetch by email
    List<NeedyDetailsDTO> getAllNeedyDetails();
}