package com.stansvec.dropwizard.auth.authorization

import com.stansvec.dropwizard.auth.Auth
import com.stansvec.dropwizard.auth.NoAuth
import com.stansvec.dropwizard.auth.roles.Admin

import javax.ws.rs.GET
import javax.ws.rs.Path

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
    @Path("/unprotected")
    @NoAuth
    void unprotectedMethod() {}
}
