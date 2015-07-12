package com.stansvec.dropwizard.auth.roles

import com.stansvec.dropwizard.auth.Role
import com.stansvec.dropwizard.auth.TestUser

import javax.ws.rs.container.ContainerRequestContext

/**
 * Test role.
 */
class Editor implements Role<TestUser> {

    @Override
    public boolean hasRole(TestUser user, ContainerRequestContext ctx) {
        return user.hasRole(TestUser.Role.EDITOR)
    }
}
