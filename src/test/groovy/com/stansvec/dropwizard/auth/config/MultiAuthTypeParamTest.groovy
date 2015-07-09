package com.stansvec.dropwizard.auth.config

import com.stansvec.dropwizard.auth.Auth
import com.stansvec.dropwizard.auth.TestUser

import javax.ws.rs.GET
import javax.ws.rs.Path

/**
 * Check exception when {@link Auth} on both type and param level.
 */
class MultiAuthTypeParamTest extends AbstractConfigurationTest {

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
    @Auth
    static class InvalidResource {
        @GET public void method(@Auth TestUser user) {}
    }
}
