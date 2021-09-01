package com.ryq.coldstoragesystem.utils;

import org.apache.shiro.authc.AuthenticationToken;

public class MyAuthenticationToken implements AuthenticationToken {

    private String username;
    private char[] password;
    private String status;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public char[] getPassword() {
        return password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public Object getPrincipal() {
        return getUsername();
    }

    @Override
    public Object getCredentials() {
        return getPassword();
    }

    public MyAuthenticationToken() {
    }

    public MyAuthenticationToken(String username, char[] password, String status) {
        this.username = username;
        this.password = password;
        this.status = status;
    }
}
