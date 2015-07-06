package com.stansvec.dropwizard.auth.test;

import com.stansvec.dropwizard.auth.Role;

import javax.ws.rs.container.ContainerRequestContext;

/**
 * Created by turtles on 22/06/15.
 */
public class Owner implements Role<User> {

    @Override
    public boolean hasRole(User principal, ContainerRequestContext ctx) {
        return false;
    }
}
