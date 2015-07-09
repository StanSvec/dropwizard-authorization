package com.stansvec.dropwizard.auth.roles

import com.stansvec.dropwizard.auth.Role
import com.stansvec.dropwizard.auth.TestUser

import javax.ws.rs.container.ContainerRequestContext

/**
 * Test role.
 */
class Admin implements Role<TestUser> {

    @Override
    public boolean hasRole(TestUser user, ContainerRequestContext ctx) {
        return "user1".equals(user.name);
    }
}
