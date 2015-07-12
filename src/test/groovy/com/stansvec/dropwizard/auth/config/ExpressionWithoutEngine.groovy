package com.stansvec.dropwizard.auth.config

import com.stansvec.dropwizard.auth.Auth

import javax.ws.rs.GET

/**
 * Check exception when expression is used and no expression engine is set.
 */
class ExpressionWithoutEngine extends AbstractConfigurationTest {

    @Override
    Object resource() {
        return new TestResource() {
            @GET @Auth(check = "expression") public void method() {}
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
