/*
 * This code is licensed under "The MIT License"
 * Copyright (c) 2015 by Stan Svec
 *
 * Please see the included 'LICENSE.txt' file for the full text of the license.
 */

package com.stansvec.dropwizard.auth.authorization

import com.stansvec.dropwizard.auth.Auth
import com.stansvec.dropwizard.auth.NoAuth
import com.stansvec.dropwizard.auth.Principal
import com.stansvec.dropwizard.auth.TestUser
import com.stansvec.dropwizard.auth.roles.Admin
import org.hamcrest.CoreMatchers

import javax.ws.rs.GET
import javax.ws.rs.Path

import static com.stansvec.dropwizard.auth.TestUser.ADMIN
import static org.junit.Assert.assertThat

/**
 * Protected by annotation on type + unprotected by {@link NoAuth} on method.
 */
@Auth(roles = Admin.class)
@Path("/protectedType")
class ProtectedTypeResource {

    @GET
    @Path("/admin")
    void protectedMethod() {}

    @GET
    @Path("/admin/injected")
    void protectedMethod(@Principal TestUser user) {
        assertThat(user.name, CoreMatchers.is(ADMIN.name))
    }

    @GET
    @Path("/unprotected")
    @NoAuth
    void unprotectedMethod() {}
}
