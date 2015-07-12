package com.stansvec.dropwizard.auth.authorization

import javax.ws.rs.GET
import javax.ws.rs.Path

/**
 * {@link ProtectedMethodsResource} + unprotected unannotated method.
 */
class ProtectedAndUnannotatedMethodsResource extends ProtectedMethodsResource {

    @GET
    @Path("/unannotated")
    void unannotatedMethod() {}
}
