/*
 * This code is licensed under "The MIT License"
 * Copyright (c) 2015 by Stan Svec
 *
 * Please see the included 'LICENSE.txt' file for the full text of the license.
 */

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
