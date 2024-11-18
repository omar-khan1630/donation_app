package com.donation.api.service.impl;

import com.donation.api.dto.DonationDTO;
import com.donation.api.entity.Donation;
import com.donation.api.entity.Donor;
import com.donation.api.entity.Needy;
import com.donation.api.exception.ResourceNotFoundException;
import com.donation.api.repository.DonationRepository;
import com.donation.api.repository.DonorRepository;
import com.donation.api.repository.NeedyRepository;
import com.donation.api.service.DonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DonationServiceImpl implements DonationService {

    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    private DonorRepository donorRepository;

    @Autowired
    private NeedyRepository needyRepository;

    @Override
    public DonationDTO createDonation(DonationDTO donationDTO) {
        Donor donor = donorRepository.findById(donationDTO.getDonorId())
                .orElseThrow(() -> new ResourceNotFoundException("Donor", "id", donationDTO.getDonorId()));
        
        Needy needy = needyRepository.findById(donationDTO.getNeedyId())
                .orElseThrow(() -> new ResourceNotFoundException("Needy", "id", donationDTO.getNeedyId()));
        
        Donation donation = new Donation();
        donation.setDonor(donor);
        donation.setNeedy(needy);
        donation.setAmount(donationDTO.getAmount());
        donation.setTransactionId(donationDTO.getTransactionId());
        donation.setIsTransferSuccessful(donationDTO.getIsTransferSuccessful());
        donation.setIsDeleted(false);

        Donation savedDonation = donationRepository.save(donation);
        
        return mapToDTO(savedDonation);
    }

    @Override
    public DonationDTO updateDonation(Integer id, DonationDTO donationDTO) {
        Donation donation = donationRepository.findByDonationIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donation", "id", id));
        
        Donor donor = donorRepository.findById(donationDTO.getDonorId())
                .orElseThrow(() -> new ResourceNotFoundException("Donor", "id", donationDTO.getDonorId()));
        
        Needy needy = needyRepository.findById(donationDTO.getNeedyId())
                .orElseThrow(() -> new ResourceNotFoundException("Needy", "id", donationDTO.getNeedyId()));
        
        donation.setDonor(donor);
        donation.setNeedy(needy);
        donation.setAmount(donationDTO.getAmount());
        donation.setTransactionId(donationDTO.getTransactionId());
        donation.setIsTransferSuccessful(donationDTO.getIsTransferSuccessful());

        Donation updatedDonation = donationRepository.save(donation);
        return mapToDTO(updatedDonation);
    }

    @Override
    public void softDeleteDonation(Integer id) {
        Donation donation = donationRepository.findByDonationIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donation", "id", id));
        donation.setIsDeleted(true);
        donationRepository.save(donation);
    }

    @Override
    public DonationDTO getDonationById(Integer id) {
        Donation donation = donationRepository.findByDonationIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donation", "id", id));
        return mapToDTO(donation);
    }

    @Override
    public List<DonationDTO> getAllDonations() {
        List<Donation> donations = donationRepository.findAllByIsDeletedFalse();
        return donations.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    private DonationDTO mapToDTO(Donation donation) {
        return new DonationDTO(
                donation.getDonationId(),
                donation.getDonor().getDonorId(),
                donation.getNeedy().getNeedyId(),
                donation.getAmount(),
                donation.getTransactionId(),
                donation.getIsTransferSuccessful()
        );
    }
}
