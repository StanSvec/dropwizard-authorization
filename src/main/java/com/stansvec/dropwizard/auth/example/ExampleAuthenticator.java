package com.stansvec.dropwizard.auth.example;

import com.google.common.base.Optional;
import com.yammer.dropwizard.auth.AuthenticationException;
import com.yammer.dropwizard.auth.Authenticator;

public class ExampleAuthenticator implements Authenticator<String, com.stansvec.dropwizard.auth.example.ExampleAuthInfo> {

    @Override
    public Optional<com.stansvec.dropwizard.auth.example.ExampleAuthInfo> authenticate(String credentials) throws AuthenticationException {
        return Optional.of(new com.stansvec.dropwizard.auth.example.ExampleAuthInfo());
    }
}
