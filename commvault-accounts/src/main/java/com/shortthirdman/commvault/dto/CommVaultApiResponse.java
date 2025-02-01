// Copyright (c) ShortThirdMan 2025. All rights reserved.
package com.shortthirdman.commvault.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.OptBoolean;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static com.shortthirdman.commvault.common.CommVaultConstants.DATE_TIME_FORMAT_WITH_TZ;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL, content = JsonInclude.Include.NON_EMPTY)
public class CommVaultApiResponse {

    private String status;

    @Builder.Default
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT_WITH_TZ, lenient = OptBoolean.TRUE)
    private ZonedDateTime timestamp = ZonedDateTime.now(ZoneId.systemDefault());

    private String message;
    private Object data;
    private String description;
}
