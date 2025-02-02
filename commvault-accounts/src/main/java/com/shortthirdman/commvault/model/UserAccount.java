// Copyright (c) ShortThirdMan 2025. All rights reserved.
package com.shortthirdman.commvault.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class UserAccount implements Serializable {

    @NotNull
    private Integer id;

    @NotEmpty
    private String orgId;

    @NotEmpty
    private String orgName;

    @NotEmpty
    private String userName;

    @NotEmpty
    private String emailAddress;

    @NotEmpty
    private String password;

    private Boolean active;

    @NotNull
    private AccountType accountType;

    private Map<String, String> metadata;

    private String createdTime;

    private String modifiedTime;
}
