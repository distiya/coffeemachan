package com.coffeemachan.accountservice.data;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class BaseAccountPrincipal {

    private String firstName;
    private String lastName;
    private String address;
    private String contactNumber;
    private String email;
    private String userName;
    private String password;
    private Boolean emailVerified;
    private Boolean enabled;
    private List<String> roles;
    private Map<String,String> attributes;

}
