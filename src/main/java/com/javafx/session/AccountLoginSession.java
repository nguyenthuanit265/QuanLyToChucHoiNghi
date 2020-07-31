package com.javafx.session;

import com.javafx.entity.Account;
import com.javafx.repository.AccountRepository;
import com.javafx.repository.impl.AccountRepositoryImpl;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class AccountLoginSession {
    Preferences userPreferences = Preferences.userRoot();
    String emailLoggedIn = userPreferences.get("email", "");
    String roleName = userPreferences.get("role", "");

    public String getEmailAccountLogin() {
        if (emailLoggedIn.length() == 0) {
            return null;
        } else {
            return emailLoggedIn;
        }
    }

    public String getRoleAccountLogin() {
        if (emailLoggedIn.length() == 0) {
            return null;
        } else {
            return roleName;
        }

    }

    public int getIdAccountLogin() {
        if (emailLoggedIn.length() == 0) {
            return -1;
        } else {
            AccountRepository accountRepository = new AccountRepositoryImpl();
//        Preferences userPreferences = Preferences.userRoot();
//        String emailLoggedIn = userPreferences.get("email", "");
//        String roleName = userPreferences.get("role", "");
            Account accountRes = accountRepository.findByEmail(emailLoggedIn);

            return accountRes.getId();
        }

    }

    public Account getAccountLogin() {
        if (emailLoggedIn.length() == 0) {
            return null;
        } else {
            AccountRepository accountRepository = new AccountRepositoryImpl();
            Account accountRes = accountRepository.findByEmail(emailLoggedIn);
            return accountRes;
        }

    }

    public boolean deleteAccountLogin() throws BackingStoreException {
        if (emailLoggedIn.length() == 0) {
            return false;
        } else {
            AccountRepository accountRepository = new AccountRepositoryImpl();
            Account accountRes = accountRepository.findByEmail(emailLoggedIn);
            userPreferences.clear();
            return true;
        }
    }
}
