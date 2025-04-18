// Copyright (c) ShortThirdMan 2025. All rights reserved.
package com.shortthirdman.commvault.service.impl;

import com.shortthirdman.commvault.common.CommVaultUtils;
import com.shortthirdman.commvault.exception.CommVaultException;
import com.shortthirdman.commvault.exception.RecordNotSavedException;
import com.shortthirdman.commvault.exception.RecordsNotFoundException;
import com.shortthirdman.commvault.model.UserAccount;
import com.shortthirdman.commvault.service.AccountsService;
import com.shortthirdman.commvault.service.CommVaultAdaptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.shortthirdman.commvault.common.CommVaultConstants.ACCOUNT_PREFIX;
import static com.shortthirdman.commvault.common.CommVaultConstants.ASTERISK;
import static com.shortthirdman.commvault.common.CommVaultConstants.COLON;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountsServiceImpl implements AccountsService {

    private final CommVaultAdaptor adaptor;

    /**
     * @return list of user accounts
     */
    @Override
    public List<UserAccount> allUserAccounts() {
        log.info("Retrieving all user accounts from cache");
        List<UserAccount> userAccounts = new ArrayList<>();
        try {
            var keys = adaptor.cacheKeys(ACCOUNT_PREFIX);
            log.info("Total user accounts for key pattern {}: {}", ACCOUNT_PREFIX, Optional.of(keys.size()));
            for (String k : keys) {
                var obj = (UserAccount) adaptor.getData(k);
                userAccounts.add(obj);
            }
            if (userAccounts.isEmpty()) {
                throw new RecordsNotFoundException("No user accounts found for key pattern " + ACCOUNT_PREFIX);
            }
        } catch (Exception e) {
            log.error("Unable to get user accounts from cache: {}", ExceptionUtils.getFullStackTrace(e));
            throw new RecordsNotFoundException(e.getMessage());
        }

        return userAccounts;
    }

    /**
     * @param orgId the organization
     * @return
     */
    @Override
    public List<UserAccount> userAccountsByOrganization(String orgId) {
        log.info("Retrieving user accounts for organization {} from cache", orgId);
        List<UserAccount> userAccounts = new ArrayList<>();
        try {
            var cacheKey = ACCOUNT_PREFIX + orgId;
            var keys = adaptor.cacheKeys(cacheKey);
            log.info("Total user accounts for key pattern with organization {}: {}", cacheKey, Optional.of(keys.size()));
            for (String k : keys) {
                var obj = (UserAccount) adaptor.getData(k);
                userAccounts.add(obj);
            }
            if (userAccounts.isEmpty()) {
                throw new RecordsNotFoundException("No user accounts found for the organization " + orgId);
            }
        } catch (Exception e) {
            log.error("Unable to get user accounts with organization {}: {}", orgId, ExceptionUtils.getFullStackTrace(e));
            throw new RecordsNotFoundException(e.getMessage());
        }

        return userAccounts;
    }

    /**
     * @param userAccount the new user account to create
     */
    @Override
    public void createUserAccount(UserAccount userAccount) {
        log.info("Saving new user account with OrgID {}", userAccount.getOrgId());
        StringBuilder cacheKey = new StringBuilder(ACCOUNT_PREFIX);
        try {
            if (userAccount.getCreatedTime() == null) {
                userAccount.setCreatedTime(CommVaultUtils.currentDateTime());
            }
            cacheKey.append(userAccount.getOrgId());
            adaptor.saveData(cacheKey.toString(), userAccount);
            log.info("New user account for key {} saved", cacheKey);
        } catch (Exception e) {
            log.error("Unable to save user account {}: {}", cacheKey, ExceptionUtils.getFullStackTrace(e));
            throw new RecordNotSavedException(e.getMessage());
        }
    }

    /**
     * @param userAccount the user account to delete
     */
    @Override
    public void deleteUserAccount(UserAccount userAccount) {
        log.info("Deleting user account with OrgID {}", userAccount.getOrgId());
        StringBuilder cacheKey = new StringBuilder(ACCOUNT_PREFIX);
        try {
            cacheKey.append(userAccount.getOrgId());
            var success = adaptor.delete(cacheKey.toString());
            log.info("User account with key {} deleted successfully [{}]", cacheKey, success);
        } catch (Exception e) {
            log.error("Unable to delete the user account with key {}: {}", cacheKey, ExceptionUtils.getFullStackTrace(e));
            throw new CommVaultException("Record with key " + cacheKey + "could not be deleted");
        }
    }

    /**
     * @param userAccount the user account to update
     */
    @Override
    public void updateUserAccount(UserAccount userAccount) {
        log.info("Attempting to update user account with organization {}", userAccount.getOrgId());
        StringBuilder cacheKey = new StringBuilder(ACCOUNT_PREFIX);
        try {
            cacheKey.append(userAccount.getOrgId());
        } catch (Exception e) {
            log.error("Unable to update the user account with key {}: {}", cacheKey, ExceptionUtils.getFullStackTrace(e));
            throw new CommVaultException("Account with key " + cacheKey + "could not be updated");
        }
    }

    /**
     * @param orgId the organization identifier
     */
    @Override
    public void deleteUserAccount(String orgId) {
        log.info("Attempting to delete user account with OrgID {}", orgId);
        StringBuilder cacheKey = new StringBuilder(ACCOUNT_PREFIX);
        try {
            cacheKey.append(orgId).append(COLON);
            var success = adaptor.delete(cacheKey.toString());
            log.info("User account with organisation {} deleted successfully [{}]", orgId, success);
        } catch (Exception e) {
            log.error("Unable to delete the user account with organization {}: {}", orgId, ExceptionUtils.getFullStackTrace(e));
            throw new CommVaultException("Account with key " + cacheKey + "could not be deleted");
        }
    }

    @Override
    public void updatePassword(String orgId, String password) {
        log.info("Attempting to update user account with OrgID {}", orgId);
        StringBuilder cacheKey = new StringBuilder(ACCOUNT_PREFIX);
        try {
            cacheKey.append(orgId);
            if (!adaptor.hasKey(cacheKey.toString())) {
                log.warn("User account with key {} does not exist", cacheKey);
                throw new IllegalStateException("Cache key does not exist");
            }
            log.info("Updating password for user account with OrgID {}", orgId);
            adaptor.updateEntry(cacheKey.toString(), "password", password);
            adaptor.updateEntry(cacheKey.toString(), "modified_time", CommVaultUtils.currentDateTime());
        } catch (Exception e) {
            log.error("Unable to update password for the user account {}: {}", orgId, ExceptionUtils.getFullStackTrace(e));
            throw new CommVaultException("Credentials for the account with org " + orgId + "could not be updated");
        }
    }
}
