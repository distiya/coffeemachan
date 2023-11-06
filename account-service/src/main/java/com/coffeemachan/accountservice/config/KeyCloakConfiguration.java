package com.coffeemachan.accountservice.config;

import com.coffeemachan.accountservice.service.AccountSecurityService;
import com.coffeemachan.accountservice.service.impl.KeyCloakAccountSecurityService;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ConditionalOnProperty(value = "coffeemachan.security-providers.keycloak.connect_url")
public class KeyCloakConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "coffeemachan.security-providers.keycloak")
    public KeyCloakConfigurationProperties registerKeyCloakConfigurationProperties(){
        return new KeyCloakConfigurationProperties();
    }


    @Bean
    public Keycloak registerKeyCloakClient(KeyCloakConfigurationProperties config){
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(config.getConnectUrl())
                .grantType(OAuth2Constants.PASSWORD)
                .realm("master")
                .clientId("admin-cli")
                .username(config.getAdmin())
                .password(config.getAdminPassword())
                .resteasyClient(new ResteasyClientBuilderImpl().connectionPoolSize(10).build()
                ).build();
        return keycloak;
    }

    @Bean
    public AccountSecurityService  registerKeyCloakCustomerSecurityService(KeyCloakConfigurationProperties config, Keycloak keycloak){
        return new KeyCloakAccountSecurityService(config,keycloak);
    }
}
