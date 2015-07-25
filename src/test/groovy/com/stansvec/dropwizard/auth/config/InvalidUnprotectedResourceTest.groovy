package com.stansvec.dropwizard.auth.config

import com.stansvec.dropwizard.auth.Auth
import com.stansvec.dropwizard.auth.ProtectionPolicy

import javax.ws.rs.GET
import javax.ws.rs.Path

/**
 * Check exception when {@link ProtectionPolicy#PROTECT_ALL} and method unprotected.
 */
class InvalidUnprotectedResourceTest extends AbstractConfigurationTest {

    @Override
    Object resource() {
        return new TestResource() {
            @GET @Auth public void protectedMethod() {}

            @GET @Path("/unprotected") public void unprotectedMethod() {}
        }
    }

    @Override
    boolean expectExceptionOnProtectAnnotated() {
        return false
    }

    @Override
    boolean expectExceptionOnProtectAll() {
        return true
    }
}
