package com.stansvec.dropwizard.auth.config

import com.stansvec.dropwizard.auth.Auth
import com.stansvec.dropwizard.auth.TestResource
import com.stansvec.dropwizard.auth.TestUser

import javax.ws.rs.GET

/**
 * Check exception when {@link Auth} on both method and param level.
 */
class MultiAuthMethodParamTest extends AbstractConfigurationTest {

    @Override
    Object resource() {
        return new TestResource() {
            @GET @Auth public void method(@Auth TestUser user) {}
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
