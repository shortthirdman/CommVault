// Copyright (c) ShortThirdMan 2025. All rights reserved.
package com.shortthirdman.commvault.exception;

public class CommVaultException extends RuntimeException {
    public CommVaultException(String message) {
        super(message);
    }

    public CommVaultException(String message, Throwable cause) {
        super(message, cause);
    }
}
