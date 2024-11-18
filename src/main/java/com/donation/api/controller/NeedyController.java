package com.donation.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.donation.api.dto.NeedyRequestDTO;
import com.donation.api.dto.NeedyResponseDTO;
import com.donation.api.response.MessageResponse;
import com.donation.api.service.NeedyService;

import java.util.List;

@RestController
@RequestMapping("/api/needy")
public class NeedyController {

    @Autowired
    private NeedyService needyService;
    @PreAuthorize("hasAnyRole('ORGANIZATION', 'VOLUNTEER')")
    @PostMapping
    public ResponseEntity<NeedyResponseDTO> createNeedy(@RequestBody NeedyRequestDTO needyRequest) {
        NeedyResponseDTO response = needyService.createNeedy(needyRequest);
        return ResponseEntity.ok(response);
    }
    // Update Needy by email (all roles should be able to access this if authenticated)
    @PutMapping("/email/{email}")
    public ResponseEntity<MessageResponse> updateNeedyByEmail(@PathVariable String email, @RequestBody NeedyRequestDTO needyRequest) {
        MessageResponse response = needyService.updateNeedyByEmail(email, needyRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeleteNeedy(@PathVariable Integer id) {
        needyService.softDeleteNeedy(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<NeedyResponseDTO> getNeedyById(@PathVariable Integer id) {
        return ResponseEntity.of(needyService.getNeedyById(id));
    }

    @GetMapping
    public ResponseEntity<List<NeedyResponseDTO>> getAllNeedies() {
        return ResponseEntity.ok(needyService.getAllNeedies());
    }
}
