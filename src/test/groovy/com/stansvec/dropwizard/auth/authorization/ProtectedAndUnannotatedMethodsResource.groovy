/*
 * This code is licensed under "The MIT License"
 * Copyright (c) 2015 by Stan Svec
 *
 * Please see the included 'LICENSE.txt' file for the full text of the license.
 */

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
