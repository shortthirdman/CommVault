package com.shortthirdman.commvault.service;

public interface OTPService {

    String generateOTP(int length) throws Exception;
    Boolean validateOTP(String otp) throws Exception;
}
