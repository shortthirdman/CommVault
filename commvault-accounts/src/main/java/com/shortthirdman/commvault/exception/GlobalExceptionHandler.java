// Copyright (c) ShortThirdMan 2025. All rights reserved.
package com.shortthirdman.commvault.exception;

import com.shortthirdman.commvault.dto.CommVaultApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import static com.shortthirdman.commvault.common.CommVaultConstants.FAILURE;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {CommVaultException.class})
    public ResponseEntity<CommVaultApiResponse> handleException(CommVaultException ex, WebRequest request) {
        log.error("Application error: {}", ExceptionUtils.getFullStackTrace(ex));
        CommVaultApiResponse httpResponse = CommVaultApiResponse.builder()
                .status(FAILURE)
                .message(ex.getMessage())
                .description("IN")
                .build();

        return new ResponseEntity<>(httpResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {RecordsNotFoundException.class})
    public ResponseEntity<CommVaultApiResponse> handleRecordsNotFoundException(RecordsNotFoundException ex, WebRequest request) {
        log.error("Records could not be found {}: {}", request.getContextPath(), ExceptionUtils.getFullStackTrace(ex));
        CommVaultApiResponse httpResponse = CommVaultApiResponse.builder()
                .status(FAILURE)
                .message(ex.getMessage())
                .description("Records not found")
                .build();

        return new ResponseEntity<>(httpResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {RecordNotSavedException.class})
    public ResponseEntity<CommVaultApiResponse> handleRecordNotSavedException(RecordNotSavedException ex, WebRequest request) {
        log.error("Records could not be saved {}: {}", request.getContextPath(), ExceptionUtils.getFullStackTrace(ex));
        CommVaultApiResponse httpResponse = CommVaultApiResponse.builder()
                .message(ex.getMessage())
                .status(FAILURE)
                .description("Failed to save record")
                .build();

        return new ResponseEntity<>(httpResponse, HttpStatus.EXPECTATION_FAILED);
    }

    @ExceptionHandler(value = {RecordAlreadyExistsException.class})
    public ResponseEntity<CommVaultApiResponse> handleRecordAlreadyExistsException(RecordAlreadyExistsException ex, WebRequest request) {
        log.error("Record already exists {}: {}", request.getContextPath(), ExceptionUtils.getFullStackTrace(ex));
        CommVaultApiResponse httpResponse = CommVaultApiResponse.builder()
                .message(ex.getMessage())
                .status(FAILURE)
                .description("Record already exists")
                .build();

        return new ResponseEntity<>(httpResponse, HttpStatus.CONFLICT);
    }
}
