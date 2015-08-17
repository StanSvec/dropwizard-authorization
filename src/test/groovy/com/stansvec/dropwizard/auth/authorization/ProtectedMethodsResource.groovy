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
import com.stansvec.dropwizard.auth.roles.Editor
import com.stansvec.dropwizard.auth.roles.Guest
import com.stansvec.dropwizard.auth.roles.SuperUser

import javax.ws.rs.GET
import javax.ws.rs.Path

import static org.junit.Assert.assertTrue

/**
 * Protected by annotation on methods or parameter + unprotected by {@link NoAuth} on method.
 */
@Path("/protectedMethods")
class ProtectedMethodsResource {

    @GET
    @Path("/admin-and-editor")
    @Auth(roles = [Admin.class, Editor.class])
    void adminAndEditor() {}

    @GET
    @Path("/admin-and-super+editor-or-guest")
    @Auth(roles = [Admin.class, SuperUser.class], anyRole = [Editor.class, Guest.class])
    void adminAndSuperPlusEditorOrGuest() {}

    @GET
    @Path("/editor-or-guest")
    @Auth(anyRole = [Editor.class, Guest.class])
    void editorOrGuest(@Principal TestUser user) {
        assertTrue(user.hasRole(TestUser.Role.EDITOR) || user.hasRole(TestUser.Role.GUEST))
    }

    @GET
    @Path("/unprotected")
    @NoAuth
    void unprotectedMethod() {}
}
