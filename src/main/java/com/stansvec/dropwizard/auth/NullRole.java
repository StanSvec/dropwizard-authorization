package com.stansvec.dropwizard.auth;

import javax.ws.rs.container.ContainerRequestContext;

/**
 * Null object for {@link Role}. Every principal has this roles. Used when no authorization is required.
 */
public class NullRole implements Role<Object> {

    @Override
    public boolean hasRole(Object principal, ContainerRequestContext ctx) {
        return true;
    }
}
