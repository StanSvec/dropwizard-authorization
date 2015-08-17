/*
 * This code is licensed under "The MIT License"
 * Copyright (c) 2015 by Stan Svec
 *
 * Please see the included 'LICENSE.txt' file for the full text of the license.
 */

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
