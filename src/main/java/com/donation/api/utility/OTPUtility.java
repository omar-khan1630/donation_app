package com.donation.api.utility;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Random;

public class OTPUtility {

    private static final int OTP_LENGTH = 6;
    private static final Random RANDOM = new SecureRandom();

    public static String generateOTP() {
        StringBuilder otp = new StringBuilder(OTP_LENGTH);
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(RANDOM.nextInt(10)); // Generate a digit between 0-9
        }
        return otp.toString();
    }

    public static LocalDateTime getExpirationTime(int minutes) {
        return LocalDateTime.now().plusMinutes(minutes); // 10 minutes expiry
    }
}
