package com.stansvec.dropwizard.auth.config

import com.stansvec.dropwizard.auth.Auth
import com.stansvec.dropwizard.auth.TestUser

import javax.ws.rs.GET
import javax.ws.rs.Path

/**
 * Check exception when auth is optional and authorization is used.
 */
class InvalidOptionalAuthParamTest3 extends AbstractConfigurationTest {

    @Override
    Object resource() {
        return new InvalidResource()
    }

    @Override
    boolean expectExceptionOnProtectAnnotated() {
        return true
    }

    @Override
    boolean expectExceptionOnProtectAll() {
        return true
    }

    @Path("/")
    static class InvalidResource {
        @GET public void method(@Auth(required = false, check = "expression") TestUser user) {}
    }
}
