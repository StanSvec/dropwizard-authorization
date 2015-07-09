package com.stansvec.dropwizard.auth.config

import com.stansvec.dropwizard.auth.Auth
import com.stansvec.dropwizard.auth.NoAuth

import javax.ws.rs.GET
import javax.ws.rs.Path

/**
 * Check exception when both {@link Auth} and {@link NoAuth} used on same level.
 */
class InvalidCombinationOnTypeTest extends AbstractConfigurationTest {

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
    @Auth @NoAuth
    static class InvalidResource {
        @GET public void method() {}
    }
}
