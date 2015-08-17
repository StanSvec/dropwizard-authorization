package com.stansvec.dropwizard.auth.config

import com.stansvec.dropwizard.auth.Auth
import com.stansvec.dropwizard.auth.Principal
import com.stansvec.dropwizard.auth.TestUser

import javax.ws.rs.GET
import javax.ws.rs.Path

/**
 * Check no exception when auth is optional and principal injection is used.
 */
class ValidOptionalAuthOnTypeTest extends AbstractConfigurationTest {

    @Override
    Object resource() {
        return new ValidResource()
    }

    @Override
    boolean expectExceptionOnProtectAnnotated() {
        return false
    }

    @Override
    boolean expectExceptionOnProtectAll() {
        return false
    }

    @Path("/")
    @Auth(required = false)
    static class ValidResource {
        @GET public void method(@Principal TestUser user) {}
    }
}
