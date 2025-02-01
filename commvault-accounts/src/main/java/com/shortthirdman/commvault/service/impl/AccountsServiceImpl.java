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

import java.util.List;
import java.util.Objects;

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
        List<UserAccount> userAccounts = null;
        StringBuilder cacheKey = new StringBuilder("account:");
        try {
            var size = redisTemplate.keys(cacheKey.toString()).size();
            log.info("Total user accounts: {}", size);
            userAccounts = (List<UserAccount>) redisTemplate.opsForValue().get(cacheKey.toString());
            if (Objects.isNull(userAccounts) && userAccounts.isEmpty()) {
                throw new RecordsNotFoundException("No user accounts found.");
            }
        } catch (Exception e) {
            log.error("Unable to get user accounts from redis: {}", ExceptionUtils.getFullStackTrace(e));
            throw new RecordsNotFoundException("");
        }

        return userAccounts;
    }

    /**
     * @param orgId the organization
     * @return
     */
    @Override
    public List<UserAccount> userAccountsByOrganization(String orgId) {
        List<UserAccount> userAccounts = List.of();
        if (userAccounts.isEmpty()) {
            throw new RecordsNotFoundException("No user accounts found for the organization " + orgId);
        }

        return userAccounts;
    }

    /**
     * @param userAccount
     */
    @Override
    public void createUserAccount(UserAccount userAccount) {
        log.info("Saving new user account with OrgID {}", userAccount.getOrgId());
        StringBuilder cacheKey = new StringBuilder("account:");
        try {
            if (userAccount.getCreatedTime() == null) {
                userAccount.setCreatedTime(CommVaultUtils.currentDateTime());
            }
            cacheKey.append(userAccount.getId()).append(COLON).append(userAccount.getOrgId());
            redisTemplate.opsForValue().set(cacheKey.toString(), userAccount);
            log.info("New user account for key {} saved", cacheKey);
        } catch (Exception e) {
            log.error("Unable to save user account {}: {}", cacheKey, ExceptionUtils.getFullStackTrace(e));
            throw new RecordNotSavedException(e.getMessage());
        }
    }

    /**
     * @param userAccount
     */
    @Override
    public void deleteUserAccount(UserAccount userAccount) {
        log.info("Deleting user account with OrgID {}", userAccount.getOrgId());
        StringBuilder cacheKey = new StringBuilder("account:");
        try {
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

    }
}
