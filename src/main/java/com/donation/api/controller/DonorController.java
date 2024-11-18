
package com.donation.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.donation.api.dto.DonorDTO;
import com.donation.api.service.DonorService;

import java.util.List;

@RestController
@RequestMapping("/api/donors")
public class DonorController {

    @Autowired
    private DonorService donorService;

    @PostMapping
    public ResponseEntity<DonorDTO> createDonor(@RequestBody DonorDTO donorDTO) {
        return ResponseEntity.ok(donorService.createDonor(donorDTO));
    }

    @PutMapping("/{email}")
    public ResponseEntity<DonorDTO> updateDonor(@PathVariable String email, @RequestBody DonorDTO donorDTO) {
        return ResponseEntity.ok(donorService.updateDonorByEmail(email, donorDTO));
    }

    @PatchMapping("/{id}/soft-delete")
    public ResponseEntity<Void> softDeleteDonor(@PathVariable Integer id) {
        donorService.softDeleteDonor(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DonorDTO> getDonorById(@PathVariable Integer id) {
        return ResponseEntity.ok(donorService.getDonorById(id));
    }

    @GetMapping
    public ResponseEntity<List<DonorDTO>> getAllDonors() {
        return ResponseEntity.ok(donorService.getAllDonors());
    }
}
