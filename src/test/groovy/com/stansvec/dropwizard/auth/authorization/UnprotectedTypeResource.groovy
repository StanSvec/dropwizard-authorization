/*
 * This code is licensed under "The MIT License"
 * Copyright (c) 2015 by Stan Svec
 *
 * Please see the included 'LICENSE.txt' file for the full text of the license.
 */

package com.stansvec.dropwizard.auth.authorization

import com.stansvec.dropwizard.auth.NoAuth

import javax.ws.rs.GET
import javax.ws.rs.Path

/**
 * Unprotected by {@link NoAuth} on type.
 */
@NoAuth
@Path("/unprotectedType")
class UnprotectedTypeResource {

    @GET
    @Path("/unprotected")
    void unprotectedMethod() {}
}
