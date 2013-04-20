package com.stansvec.dropwizard.auth.example;

import com.google.common.base.Optional;
import com.yammer.dropwizard.auth.AuthenticationException;
import com.yammer.dropwizard.auth.Authenticator;

public class ExampleAuthenticator implements Authenticator<String, ExampleAuthInfo> {

    @Override
    public Optional<ExampleAuthInfo> authenticate(String credentials) throws AuthenticationException {
        return Optional.of(new ExampleAuthInfo());
    }
}
