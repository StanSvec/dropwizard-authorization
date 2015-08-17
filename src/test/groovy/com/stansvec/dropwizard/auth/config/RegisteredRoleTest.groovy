/*
 * This code is licensed under "The MIT License"
 * Copyright (c) 2015 by Stan Svec
 *
 * Please see the included 'LICENSE.txt' file for the full text of the license.
 */

package com.stansvec.dropwizard.auth.config

import com.stansvec.dropwizard.auth.Auth
import com.stansvec.dropwizard.auth.roles.Admin

import javax.ws.rs.GET
import javax.ws.rs.Path

/**
 * Check no exception when registered role is used.
 */
class RegisteredRoleTest extends AbstractConfigurationTest {

    @Override
    Object resource() {
        return new ValidResource()
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
    static class ValidResource {
        @GET @Auth(roles = Admin.class) public void method() {}
    }
}
