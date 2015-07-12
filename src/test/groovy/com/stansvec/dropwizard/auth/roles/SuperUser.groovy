package com.stansvec.dropwizard.auth.roles

import com.stansvec.dropwizard.auth.Role
import com.stansvec.dropwizard.auth.TestUser

import javax.ws.rs.container.ContainerRequestContext

/**
 * Test role.
 */
class SuperUser implements Role<TestUser> {

    @Override
    public boolean hasRole(TestUser user, ContainerRequestContext ctx) {
        return user.hasRole(TestUser.Role.SUPER_USER)
    }
}
