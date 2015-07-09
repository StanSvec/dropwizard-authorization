package com.stansvec.dropwizard.auth.config

import com.stansvec.dropwizard.auth.Auth
import com.stansvec.dropwizard.auth.TestUser
import com.stansvec.dropwizard.auth.roles.Admin

import javax.ws.rs.GET
import javax.ws.rs.Path

/**
 * Check exception when auth is optional and authorization is used.
 */
class InvalidOptionalAuthParamTest2 extends AbstractConfigurationTest {

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
        @GET public void method(@Auth(required = false, anyRole = Admin.class) TestUser user) {}
    }
}
