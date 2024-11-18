package com.donation.api.forgetpassword.service;

import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class OTPStorageService {

    private final Map<String, OTPData> otpStore = new HashMap<>();

    public void storeOTP(String email, String otp, LocalDateTime expirationTime) {
        otpStore.put(email, new OTPData(otp, expirationTime));
    }

    public boolean validateOTP(String email, String inputOtp) {
        OTPData otpData = otpStore.get(email);
        if (otpData != null) {
            if (otpData.getExpirationTime().isAfter(LocalDateTime.now()) &&
                otpData.getOtp().equals(inputOtp)) {
                return true;
            }
        }
        return false;
    }

    private static class OTPData {
        private String otp;
        private LocalDateTime expirationTime;

        public OTPData(String otp, LocalDateTime expirationTime) {
            this.otp = otp;
            this.expirationTime = expirationTime;
        }

        public String getOtp() {
            return otp;
        }

        public LocalDateTime getExpirationTime() {
            return expirationTime;
        }
    }
}
