package com.coffeemachan.accountservice.service.impl;

import com.coffeemachan.accountservice.config.KeyCloakConfigurationProperties;
import com.coffeemachan.accountservice.data.BaseAccountPrincipal;
import com.coffeemachan.accountservice.service.AccountSecurityService;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Slf4j
public class KeyCloakAccountSecurityService implements AccountSecurityService {

    private final KeyCloakConfigurationProperties config;
    private final Keycloak keycloak;

    @Override
    public void persistAccount(BaseAccountPrincipal accountPrincipal) {
        RealmResource realmResource = keycloak.realm(config.getRealm());
        UsersResource usersResource = realmResource.users();
        Optional<UserRepresentation> existingProfile = searchKeyCloakByUserName(usersResource,accountPrincipal.getUserName());
        if(existingProfile.isEmpty()){
            UserRepresentation userRepresentationToBeCreated = toUserRepresentation(accountPrincipal);
            setAccountAttributes(userRepresentationToBeCreated,accountPrincipal);
            Response response = usersResource.create(userRepresentationToBeCreated);
            if(response.getStatus() == 201){
                String userId = CreatedResponseUtil.getCreatedId(response);
                setAccountRoles(realmResource,usersResource.get(userId),accountPrincipal);
            }
            log.info("Creating account in keycloak completed with status {}",response.getStatus());
        }
        else{
            UserRepresentation existingUserRepresentation = existingProfile.get();
            patchUserRepresentation(existingUserRepresentation,accountPrincipal);
            UserResource userResource = usersResource.get(existingUserRepresentation.getId());
            userResource.update(existingUserRepresentation);
            setAccountRoles(realmResource,userResource,accountPrincipal);
            log.info("Updating account is successful");
        }
    }

    @Override
    public void deleteAccount(String userName) {
        UsersResource usersResource = keycloak.realm(config.getRealm()).users();
        searchKeyCloakByUserName(usersResource,userName)
                .ifPresent(v -> usersResource.delete(v.getId()));
        log.info("Deleting account is successful");
    }

    @Override
    public AccessTokenResponse loginAccount(BaseAccountPrincipal accountPrincipal) {
        return initiateLoginForClient(accountPrincipal);
    }

    @Override
    public void logoutAccount(BaseAccountPrincipal accountPrincipal) {
        UsersResource usersResource = keycloak.realm(config.getRealm()).users();
        searchKeyCloakByUserName(usersResource,accountPrincipal.getUserName())
                .ifPresent(v -> usersResource.get(v.getId()).logout());
    }

    private AccessTokenResponse initiateLoginForClient(BaseAccountPrincipal accountPrincipal){
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(config.getConnectUrl())
                .grantType(OAuth2Constants.PASSWORD)
                .realm(config.getRealm())
                .clientId(config.getClientId())
                .clientSecret(config.getClientSecret())
                .username(accountPrincipal.getUserName())
                .password(accountPrincipal.getPassword())
                .resteasyClient(new ResteasyClientBuilderImpl().connectionPoolSize(1).build()
                ).build();
        return keycloak.tokenManager().getAccessToken();
    }

    private Optional<UserRepresentation> searchKeyCloakByUserName(UsersResource usersResource, String userName){
        return usersResource.searchByUsername(userName, true).stream().findFirst();
    }

    private void patchUserRepresentation(UserRepresentation existing, BaseAccountPrincipal accountPrincipal){
        patchBaseUserRepresentation(existing,accountPrincipal);
        setAccountAttributes(existing,accountPrincipal);
    }

    private void setAccountRoles(RealmResource realmResource, UserResource userResource, BaseAccountPrincipal accountPrincipal){
        if(!CollectionUtils.isEmpty(accountPrincipal.getRoles())){
            List<RoleRepresentation> rolesToBeAssigned = realmResource.roles().list().stream().filter(v -> accountPrincipal.getRoles().contains(v.getName())).collect(Collectors.toList());
            List<RoleRepresentation> rolesToBeRemoved = realmResource.roles().list().stream().filter(v -> !accountPrincipal.getRoles().contains(v.getName())).collect(Collectors.toList());
            userResource.roles().realmLevel().remove(rolesToBeRemoved);
            userResource.roles().realmLevel().add(rolesToBeAssigned);
        }
    }

    private void setAccountAttributes(UserRepresentation existing, BaseAccountPrincipal accountPrincipal){
        setDefaultAccountAttributes(existing,accountPrincipal);
        setCustomAccountAttributes(existing,accountPrincipal);
    }

    private void setCustomAccountAttributes(UserRepresentation existing, BaseAccountPrincipal accountPrincipal){
        if(!CollectionUtils.isEmpty(accountPrincipal.getAttributes())){
            Map<String,List<String>> attributeMap = getAttributeMapForUserRepresentation(existing);
            accountPrincipal.getAttributes().forEach((k,v)->attributeMap.put(k,Collections.singletonList(v)));
            existing.setAttributes(attributeMap);
        }
    }

    private void setDefaultAccountAttributes(UserRepresentation existing, BaseAccountPrincipal accountPrincipal){
        if(!Strings.isBlank(accountPrincipal.getAddress()) || !Strings.isBlank(accountPrincipal.getContactNumber())){
            Map<String,List<String>> attributeMap = getAttributeMapForUserRepresentation(existing);
            if(!Strings.isBlank(accountPrincipal.getAddress())){
                attributeMap.put("address",Collections.singletonList(accountPrincipal.getAddress()));
            }
            if(!Strings.isBlank(accountPrincipal.getContactNumber())){
                attributeMap.put("contactNumber",Collections.singletonList(accountPrincipal.getContactNumber()));
            }
            existing.setAttributes(attributeMap);
        }
    }

    private Map<String,List<String>> getAttributeMapForUserRepresentation(UserRepresentation existing){
        if(CollectionUtils.isEmpty(existing.getAttributes())){
            return new HashMap<>();
        }
        return existing.getAttributes();
    }

    private void patchBaseUserRepresentation(UserRepresentation initial, BaseAccountPrincipal accountPrincipal){
        initial.setEmail(accountPrincipal.getEmail() != null ? accountPrincipal.getEmail() : initial.getEmail());
        initial.setEmailVerified(accountPrincipal.getEmailVerified() != null ? accountPrincipal.getEmailVerified() : initial.isEmailVerified());
        initial.setEnabled(accountPrincipal.getEnabled() != null ? accountPrincipal.getEnabled() : initial.isEnabled());
        initial.setFirstName(accountPrincipal.getFirstName() != null ? accountPrincipal.getFirstName() : initial.getFirstName());
        initial.setLastName(accountPrincipal.getLastName() != null ? accountPrincipal.getLastName() : initial.getLastName());
        if(accountPrincipal.getPassword() != null){
            initial.setCredentials(toCredentialRepresentation(accountPrincipal));
        }
        if(!CollectionUtils.isEmpty(accountPrincipal.getRoles())){
            initial.setRealmRoles(accountPrincipal.getRoles());
        }
    }

    private UserRepresentation toUserRepresentation(BaseAccountPrincipal accountPrincipal){
        UserRepresentation userRepresentation = new UserRepresentation();
        patchBaseUserRepresentation(userRepresentation,accountPrincipal);
        userRepresentation.setUsername(accountPrincipal.getUserName());
        return userRepresentation;
    }

    private List<CredentialRepresentation> toCredentialRepresentation(BaseAccountPrincipal accountPrincipal){
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(accountPrincipal.getPassword());
        return Stream.of(credentialRepresentation).collect(Collectors.toList());
    }
}
