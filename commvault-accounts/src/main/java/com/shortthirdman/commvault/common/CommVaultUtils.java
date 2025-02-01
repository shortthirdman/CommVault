// Copyright (c) ShortThirdMan 2025. All rights reserved.
package com.shortthirdman.commvault.common;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public final class CommVaultUtils {

    private CommVaultUtils() {
    }

    public static String currentDateTime() {
        var now = LocalDateTime.now();
        log.debug("Current timestamp: {}", now);
        var formatter = DateTimeFormatter.ofPattern(CommVaultConstants.DATE_TIME_FORMAT);
        return now.format(formatter);
    }
}
