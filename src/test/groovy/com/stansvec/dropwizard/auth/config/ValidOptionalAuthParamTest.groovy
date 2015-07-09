package com.stansvec.dropwizard.auth.config

import com.stansvec.dropwizard.auth.Auth
import com.stansvec.dropwizard.auth.TestResource
import com.stansvec.dropwizard.auth.TestUser

import javax.ws.rs.GET

/**
 * Check no exception when auth is optional on param level.
 */
class ValidOptionalAuthParamTest extends AbstractConfigurationTest {

    @Override
    Object resource() {
        return new TestResource() {
            @GET public void method(@Auth(required = false) TestUser user) {}
        }
    }

    @Override
    boolean expectExceptionOnProtectAnnotated() {
        return false
    }

    @Override
    boolean expectExceptionOnProtectAll() {
        return false
    }
}
