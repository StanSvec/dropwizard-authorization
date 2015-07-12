package com.stansvec.dropwizard.auth

import com.google.common.base.Optional
import io.dropwizard.auth.AuthenticationException
import io.dropwizard.auth.Authenticator
import io.dropwizard.auth.basic.BasicAuthFactory
import io.dropwizard.auth.basic.BasicCredentials

import static com.stansvec.dropwizard.auth.TestUser.USERS

/**
 * Authenticator returning user if name is user1 and password is pass1.
 */
class TestAuthenticator implements Authenticator<BasicCredentials, TestUser> {

    static def AUTH_FACT = new BasicAuthFactory<>(new TestAuthenticator(), "realm", TestUser.class)

    static def ROLES = USERS.collectEntries{[(it.name) : it]}

    @Override
    Optional<TestUser> authenticate(BasicCredentials credentials) throws AuthenticationException {
        return Optional.fromNullable(ROLES[credentials.username])
    }
}
