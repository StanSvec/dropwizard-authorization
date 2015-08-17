package com.stansvec.dropwizard.auth.config

import com.stansvec.dropwizard.auth.Auth
import com.stansvec.dropwizard.auth.Principal
import com.stansvec.dropwizard.auth.TestUser
import com.stansvec.dropwizard.auth.roles.Admin

import javax.ws.rs.GET
import javax.ws.rs.Path

import static org.junit.Assert.fail

/**
 * Check exception when auth is optional and authorization is used.
 */
class InvalidOptionalAuthorizationUsedTest1 extends AbstractConfigurationTest {

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
    @Auth(required = false, roles = Admin.class)
    static class InvalidResource {

        @GET public void method(@Principal TestUser user) {
            fail()
        }
    }
}
