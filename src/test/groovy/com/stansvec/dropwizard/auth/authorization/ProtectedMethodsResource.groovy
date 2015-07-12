package com.stansvec.dropwizard.auth.authorization

import com.stansvec.dropwizard.auth.Auth
import com.stansvec.dropwizard.auth.NoAuth
import com.stansvec.dropwizard.auth.TestUser
import com.stansvec.dropwizard.auth.roles.Admin
import com.stansvec.dropwizard.auth.roles.Editor
import com.stansvec.dropwizard.auth.roles.Guest
import com.stansvec.dropwizard.auth.roles.SuperUser

import javax.ws.rs.GET
import javax.ws.rs.Path

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
    void EditorOrGuest(@Auth(anyRole = [Editor.class, Guest.class]) TestUser user) {}

    @GET
    @Path("/unprotected")
    @NoAuth
    void unprotectedMethod() {}
}
