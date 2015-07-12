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
