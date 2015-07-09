package com.stansvec.dropwizard.auth.config

import com.stansvec.dropwizard.auth.Auth
import com.stansvec.dropwizard.auth.TestResource

import javax.ws.rs.GET

/**
 * Check exception when auth is optional on method level.
 */
class InvalidOptionalAuthMethodTest extends AbstractConfigurationTest {

    @Override
    Object resource() {
        return new TestResource() {
            @GET @Auth(required = false) public void method() {}
        }
    }

    @Override
    boolean expectExceptionOnProtectAnnotated() {
        return true
    }

    @Override
    boolean expectExceptionOnProtectAll() {
        return true
    }
}
