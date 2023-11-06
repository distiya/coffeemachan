package com.coffeemachan.accountservice.controller;

import com.coffeemachan.accountservice.data.BaseAccountPrincipal;
import com.coffeemachan.accountservice.service.AccountSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@RestController
@RequestMapping("/customers")
public class CustomerAccountController {

    private final AccountSecurityService accountSecurityService;

    private List<String> defaultCustomerRoles = Stream.of("ROLE_CUSTOMER").collect(Collectors.toList());

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    private void createCustomerAccount(@RequestBody BaseAccountPrincipal accountPrincipal){
        accountPrincipal.setRoles(defaultCustomerRoles);
        accountSecurityService.persistAccount(accountPrincipal);
    }

    @PostMapping(path = "/loyalty-points", consumes = MediaType.APPLICATION_JSON_VALUE)
    private void setLoyaltyPoints(@RequestBody BaseAccountPrincipal accountPrincipal){
        accountSecurityService.persistAccount(accountPrincipal);
    }

}
