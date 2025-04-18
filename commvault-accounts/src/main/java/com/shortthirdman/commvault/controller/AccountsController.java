// Copyright (c) ShortThirdMan 2025. All rights reserved.
package com.shortthirdman.commvault.controller;

import com.shortthirdman.commvault.dto.CommVaultApiResponse;
import com.shortthirdman.commvault.model.UserAccount;
import com.shortthirdman.commvault.service.AccountsService;
import com.shortthirdman.commvault.swagger.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static com.shortthirdman.commvault.common.CommVaultConstants.SUCCESS;

@RestController
@CrossOrigin(value = {"*"})
@RequiredArgsConstructor
@Tag(name = "Accounts", description = "API endpoints to manage operations on user accounts")
@RequestMapping(value = "/api/accounts", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROTOBUF_VALUE})
public class AccountsController {

    private final AccountsService accountsService;

    @GetAllAccounts
    @GetMapping(path = {""})
    public ResponseEntity<CommVaultApiResponse> getUserAccounts() {
        CommVaultApiResponse response = CommVaultApiResponse.builder()
                .data(accountsService.allUserAccounts())
                .status(SUCCESS)
                .message("")
                .build();

        return ResponseEntity.ok(response);
    }

    @GetAccountsByType
    @GetMapping(path = "/account-type/{type}")
    public ResponseEntity<CommVaultApiResponse> getAccountsByType(@PathVariable(name = "type") String type) {
        List<UserAccount> accountsByType = accountsService.userAccountsByOrganization(type);
        CommVaultApiResponse response = CommVaultApiResponse.builder()
                .data(accountsByType)
                .status(SUCCESS)
                .message("")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetOrgUserAccount
    @GetMapping(path = "/organization/{orgId}")
    public ResponseEntity<CommVaultApiResponse> getOrgUserAccount(@PathVariable(name = "orgId") String orgId) {
        List<UserAccount> accountsByType = accountsService.userAccountsByOrganization(orgId);
        CommVaultApiResponse response = CommVaultApiResponse.builder()
                .data(accountsByType)
                .status(SUCCESS)
                .message("")
                .build();
        return ResponseEntity.ok(response);
    }

    @CreateNewAccount
    @PostMapping(path = "/new")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CommVaultApiResponse> addNewUserAccount(@Valid @RequestBody UserAccount account) {
        accountsService.createUserAccount(account);
        CommVaultApiResponse response = CommVaultApiResponse.builder()
                .status(SUCCESS)
                .message("New user account created successfully")
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @UpdateUserAccount
    @PutMapping(path = "/update")
    public ResponseEntity<CommVaultApiResponse> updateExistingUserAccount(@Valid @RequestBody UserAccount account) {
        accountsService.updateUserAccount(account);
        CommVaultApiResponse response = CommVaultApiResponse.builder()
                .status(SUCCESS)
                .message("")
                .description("")
                .build();
        return ResponseEntity.ok(response);
    }

    @RemoveUserAccount
    @DeleteMapping(path = "/{orgId}")
    public ResponseEntity<CommVaultApiResponse> deleteUserAccount(@PathVariable(name = "orgId") @NonNull String orgId) {
        accountsService.deleteUserAccount(orgId);
        CommVaultApiResponse response = CommVaultApiResponse.builder()
                .status(SUCCESS)
                .message("")
                .description("")
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping(path = "/update")
    public ResponseEntity<CommVaultApiResponse> updateAccountPassword(@Valid @RequestBody Map<String, String> accountMap) {
        accountsService.updatePassword(accountMap.get("orgId"), accountMap.get("password"));
        CommVaultApiResponse response = CommVaultApiResponse.builder()
                .status(SUCCESS)
                .message("")
                .description("")
                .build();
        return ResponseEntity.ok(response);
    }
}
