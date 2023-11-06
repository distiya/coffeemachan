package com.coffeemachan.accountservice.service.impl;

import com.coffeemachan.accountservice.data.BaseAccountPrincipal;
import com.coffeemachan.accountservice.service.AccountSecurityService;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.AccessTokenResponse;

@Slf4j
public class TestAccountSecurityService implements AccountSecurityService {

    @Override
    public void persistAccount(BaseAccountPrincipal accountPrincipal) {
        log.info("Executing test persist customer account");
    }

    @Override
    public void deleteAccount(String userName) {
        log.info("Executing test delete account");
    }

    @Override
    public AccessTokenResponse loginAccount(BaseAccountPrincipal accountPrincipal) {
        log.info("Executing test login account");
        return null;
    }

    @Override
    public void logoutAccount(BaseAccountPrincipal accountPrincipal) {
        log.info("Executing test logout account");
    }
}
