package com.coffeemachan.accountservice.controller;

import com.coffeemachan.accountservice.data.BaseAccountPrincipal;
import com.coffeemachan.accountservice.service.AccountSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/employees")
public class EmployeeAccountController {

    private final AccountSecurityService accountSecurityService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    private void createCustomerAccount(@RequestBody BaseAccountPrincipal accountPrincipal){
        accountSecurityService.persistAccount(accountPrincipal);
    }

}
