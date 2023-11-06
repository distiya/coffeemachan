package com.coffeemachan.accountservice.controller;

import com.coffeemachan.accountservice.data.BaseAccountPrincipal;
import com.coffeemachan.accountservice.service.AccountSecurityService;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountSecurityService accountSecurityService;

    @DeleteMapping
    private void deleteCustomerAccount(@RequestParam String userName){
        accountSecurityService.deleteAccount(userName);
    }

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    private AccessTokenResponse loginCustomerAccountAccount(@RequestBody BaseAccountPrincipal accountPrincipal){
        return accountSecurityService.loginAccount(accountPrincipal);
    }

    @PostMapping(path = "/logout", consumes = MediaType.APPLICATION_JSON_VALUE)
    private void logoutCustomerAccountAccount(@RequestBody BaseAccountPrincipal accountPrincipal){
       accountSecurityService.logoutAccount(accountPrincipal);
    }

}
