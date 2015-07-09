package com.stansvec.dropwizard.auth

import com.google.common.base.Optional
import io.dropwizard.auth.AuthenticationException
import io.dropwizard.auth.Authenticator
import io.dropwizard.auth.basic.BasicAuthFactory
import io.dropwizard.auth.basic.BasicCredentials

/**
 * Authenticator returning user if name is user1 and password is pass1.
 */
class TestAuthenticator implements Authenticator<BasicCredentials, TestUser> {

    static def AUTH_FACT = new BasicAuthFactory<>(new TestAuthenticator(), "realm", TestUser.class)

    @Override
    Optional<TestUser> authenticate(BasicCredentials credentials) throws AuthenticationException {
        if (credentials.username == "user1" && credentials.password == "pass1") {
            return Optional.of(new TestUser("user1"))
        }

        return Optional.absent()
    }
}
