package com.stansvec.dropwizard.auth.authorization

import javax.ws.rs.GET
import javax.ws.rs.Path

/**
 * Unprotected unannotated resource.
 */
@Path("/unannotatedType")
class UnannotatedTypeResource {

    @GET
    @Path("/unprotected")
    void unprotectedMethod() {}
}
