package com.coffeemachan.accountservice.config;

import lombok.Data;

@Data
public class KeyCloakConfigurationProperties {

    private String admin;
    private String adminPassword;
    private String connectUrl;
    private String realm;
    private String clientId;
    private String clientSecret;

}
