package com.stansvec.dropwizard.auth.config

import com.stansvec.dropwizard.auth.Auth

import javax.ws.rs.GET
import javax.ws.rs.Path

/**
 * Check exception when auth is optional on type level and no principal injection is used.
 */
class InvalidOptionalOnTypeNoInjectionTest extends AbstractConfigurationTest {

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
    @Auth(required = false)
    static class InvalidResource {
        @GET public void method() {}
    }
}
