package com.stansvec.dropwizard.auth.config

import com.stansvec.dropwizard.auth.Auth
import com.stansvec.dropwizard.auth.NoAuth

import javax.ws.rs.GET

/**
 * Check exception when both {@link Auth} and {@link NoAuth} used on same level.
 */
class InvalidCombinationOnMethodTest extends AbstractConfigurationTest {

    @Override
    Object resource() {
        return new TestResource() {
            @GET @Auth @NoAuth public void method() {}
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
