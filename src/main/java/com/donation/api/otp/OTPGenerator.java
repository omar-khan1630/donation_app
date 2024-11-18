package com.donation.api.otp;

import java.security.SecureRandom;

public class OTPGenerator {

    private static final String NUMERIC_CHARACTERS = "0123456789";
    private static final int OTP_LENGTH = 6;
    private static final SecureRandom random = new SecureRandom();

    public static String generateNumericOTP() {
        StringBuilder otp = new StringBuilder(OTP_LENGTH);
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(NUMERIC_CHARACTERS.charAt(random.nextInt(NUMERIC_CHARACTERS.length())));
        }
        return otp.toString();
    }
}
