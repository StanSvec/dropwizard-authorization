/*
 * This code is licensed under "The MIT License"
 * Copyright (c) 2015 by Stan Svec
 *
 * Please see the included 'LICENSE.txt' file for the full text of the license.
 */

package com.stansvec.dropwizard.auth.config

import com.stansvec.dropwizard.auth.Auth
import com.stansvec.dropwizard.auth.NoAuth

import javax.ws.rs.GET
import javax.ws.rs.Path

/**
 * Check no exception when {@link NoAuth} is used on lower level than {@link Auth}.
 */
class ValidUnprotectedMethodTest extends AbstractConfigurationTest {

    @Override
    Object resource() {
        return new InvalidResource()
    }

    @Override
    boolean expectExceptionOnProtectAnnotated() {
        return false
    }

    @Override
    boolean expectExceptionOnProtectAll() {
        return false
    }

    @Path("/")
    @Auth
    static class InvalidResource {
        @GET @NoAuth public void unprotectedMethod() {}

        @GET @Path("/protected") public void protectedMethod() {}
    }
}
