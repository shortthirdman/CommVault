// Copyright (c) ShortThirdMan 2025. All rights reserved.
package com.shortthirdman.commvault.service.impl;

import com.shortthirdman.commvault.common.CommVaultUtils;
import com.shortthirdman.commvault.exception.CommVaultException;
import com.shortthirdman.commvault.exception.RecordNotSavedException;
import com.shortthirdman.commvault.exception.RecordsNotFoundException;
import com.shortthirdman.commvault.model.UserAccount;
import com.shortthirdman.commvault.service.AccountsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.shortthirdman.commvault.common.CommVaultConstants.ASTERISK;
import static com.shortthirdman.commvault.common.CommVaultConstants.COLON;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountsServiceImpl implements AccountsService {

    private final RedisTemplate<String, UserAccount> redisTemplate;

    /**
     * @return list of user accounts
     */
    @Override
    public List<UserAccount> allUserAccounts() {
        log.info("Retrieving all user accounts from cache");
        List<UserAccount> userAccounts = new ArrayList<>();
        StringBuilder cacheKey = new StringBuilder("account:");
        try {
            cacheKey.append(ASTERISK);
            var keys = redisTemplate.keys(cacheKey.toString());
            log.info("Total user accounts for key pattern {}: {}", cacheKey, keys.size());
            for (String k : keys) {
                UserAccount obj = redisTemplate.opsForValue().get(k);
                userAccounts.add(obj);
            }
            if (userAccounts.isEmpty()) {
                throw new RecordsNotFoundException("No user accounts found for key pattern " + cacheKey);
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
        List<UserAccount> userAccounts = new ArrayList<>();
        StringBuilder cacheKey = new StringBuilder("account:");
        try {
            cacheKey.append(orgId);
            var keys = redisTemplate.keys(cacheKey.toString());
            log.info("Total user accounts for key pattern with organization {}: {}", cacheKey, keys.size());
            for (String k : keys) {
                UserAccount obj = redisTemplate.opsForValue().get(k);
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
        StringBuilder cacheKey = new StringBuilder("account:");
        try {
            if (userAccount.getCreatedTime() == null) {
                userAccount.setCreatedTime(CommVaultUtils.currentDateTime());
            }
            cacheKey.append(userAccount.getOrgId());
            redisTemplate.opsForValue().set(cacheKey.toString(), userAccount);
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
        StringBuilder cacheKey = new StringBuilder("account:");
        try {
            cacheKey.append(userAccount.getOrgId());
            redisTemplate.opsForValue().getAndDelete(cacheKey.toString());
            log.info("User account with key {} deleted successfully", cacheKey);
        } catch (Exception e) {
            log.error("Unable to delete the user account with key {}: {}", cacheKey, ExceptionUtils.getFullStackTrace(e));
            throw new CommVaultException("Record with key " + cacheKey + "could not be deleted");
        }
    }

    /**
     * @param userAccount
     */
    @Override
    public void updateUserAccount(UserAccount userAccount) {
        log.info("Attempting to update user account with organization {}", userAccount.getOrgId());
        StringBuilder cacheKey = new StringBuilder("account:");
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
        StringBuilder cacheKey = new StringBuilder("account:");
        try {
            cacheKey.append(orgId).append(COLON);
            redisTemplate.opsForValue().getAndDelete(cacheKey.toString());
        } catch (Exception e) {
            log.error("Unable to delete the user account with organization {}: {}", orgId, ExceptionUtils.getFullStackTrace(e));
            throw new CommVaultException("Account with key " + cacheKey + "could not be deleted");
        }
    }
}
