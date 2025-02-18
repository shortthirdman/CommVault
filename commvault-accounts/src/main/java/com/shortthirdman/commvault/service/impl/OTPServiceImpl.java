package com.shortthirdman.commvault.service.impl;

import com.shortthirdman.commvault.exception.CommVaultException;
import com.shortthirdman.commvault.service.CommVaultAdaptor;
import com.shortthirdman.commvault.service.OTPService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;

import static com.shortthirdman.commvault.common.CommVaultConstants.OTP_PREFIX;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OTPServiceImpl implements OTPService {

    private final CommVaultAdaptor adaptor;

    @Override
    public String generateOTP(int length) throws Exception {
        if (length < 1) {
            throw new CommVaultException("OTP length must be greater than 1");
        }

        log.info("Generating OTP of length {}...", length);
        String allowedCharacters = "0123456789";

        StringBuilder otp = new StringBuilder();
        try {
            SecureRandom secureRandom = new SecureRandom();

            for (int i = 0; i < length; i++) {
                int randomIndex = secureRandom.nextInt(allowedCharacters.length());
                otp.append(allowedCharacters.charAt(randomIndex));
            }
        } catch (Exception e) {
            log.error("Failed to generate {}-digit length OTP: {}", length, ExceptionUtils.getFullStackTrace(e));
            throw new CommVaultException(e.getMessage());
        }

        return otp.toString();
    }

    @Override
    public Boolean validateOTP(String otp) throws Exception {
        log.info("Validating OTP of length {}...", otp.length());
        var otpKey = OTP_PREFIX + "";
        // phoneNumber
        try {
            var storedOTP = adaptor.getData(otpKey);

            if (storedOTP != null && storedOTP.equals(otp)) {
                return adaptor.deleteKey(otpKey);
            }
        } catch (Exception e) {
            log.error("Failed to validate OTP [{}]: {}", otp, ExceptionUtils.getFullStackTrace(e));
            throw new CommVaultException(e.getMessage());
        }

        return false;
    }
}
