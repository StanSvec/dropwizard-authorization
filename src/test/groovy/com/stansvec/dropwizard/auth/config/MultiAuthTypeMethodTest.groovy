package com.stansvec.dropwizard.auth.config

import com.stansvec.dropwizard.auth.Auth

import javax.ws.rs.GET
import javax.ws.rs.Path

/**
 * Check exception when {@link Auth} on both type and method level.
 */
class MultiAuthTypeMethodTest extends AbstractConfigurationTest {

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
        @GET @Auth public void method() {}
    }
}
