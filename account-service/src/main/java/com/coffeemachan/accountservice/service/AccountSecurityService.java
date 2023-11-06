package com.coffeemachan.accountservice.service;


import com.coffeemachan.accountservice.data.BaseAccountPrincipal;
import org.keycloak.representations.AccessTokenResponse;

public interface AccountSecurityService {

    void persistAccount(BaseAccountPrincipal accountPrincipal);
    void deleteAccount(String userName);
    AccessTokenResponse loginAccount(BaseAccountPrincipal accountPrincipal);
    void logoutAccount(BaseAccountPrincipal accountPrincipal);

}
