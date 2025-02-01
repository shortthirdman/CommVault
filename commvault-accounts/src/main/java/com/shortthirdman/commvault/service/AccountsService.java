// Copyright (c) ShortThirdMan 2025. All rights reserved.
package com.shortthirdman.commvault.service;

import com.shortthirdman.commvault.model.UserAccount;

import java.util.List;

public interface AccountsService {

    List<UserAccount> allUserAccounts();

    List<UserAccount> userAccountsByOrganization(String orgId);

    void createUserAccount(UserAccount userAccount);

    void deleteUserAccount(UserAccount userAccount);

    void updateUserAccount(UserAccount userAccount);
}
