package com.stansvec.dropwizard.auth.config

import com.stansvec.dropwizard.auth.Auth
import com.stansvec.dropwizard.auth.Principal
import com.stansvec.dropwizard.auth.TestUser

import javax.ws.rs.GET

/**
 * Check no exception when auth is optional and principal injection is used.
 */
class ValidOptionalAuthOnMethodTest extends AbstractConfigurationTest {

    @Override
    Object resource() {

        return new TestResource() {

            @GET
            @Auth(required = false)
            public void method(@Principal TestUser user) {}
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
