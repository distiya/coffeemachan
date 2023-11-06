package com.coffeemachan.accountservice.config;

import com.coffeemachan.accountservice.service.AccountSecurityService;
import com.coffeemachan.accountservice.service.impl.TestAccountSecurityService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class TestConfiguration {

    @Bean("keyCloakCustomerSecurityService")
    @Primary
    public AccountSecurityService registerKeyCloakCustomerSecurityService(){
        return new TestAccountSecurityService();
    }

    @Bean("keyCloakEmployeeSecurityService")
    public AccountSecurityService  registerKeyCloakEmployeeSecurityService(){
        return new TestAccountSecurityService();
    }
}
