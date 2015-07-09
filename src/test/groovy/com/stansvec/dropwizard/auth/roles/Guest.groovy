package com.stansvec.dropwizard.auth.roles

import com.stansvec.dropwizard.auth.Role
import com.stansvec.dropwizard.auth.TestUser

import javax.ws.rs.container.ContainerRequestContext

/**
 * Test role.
 */
class Guest implements Role<TestUser> {

    @Override
    public boolean hasRole(TestUser user, ContainerRequestContext ctx) {
        return "guest".equals(user.name);
    }
}
