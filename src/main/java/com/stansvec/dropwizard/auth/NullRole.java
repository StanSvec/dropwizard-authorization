package com.stansvec.dropwizard.auth;

import javax.ws.rs.container.ContainerRequestContext;

/**
 * Null object for {@link Role}. Every principal has this roles. Used when no authorization is required.
 */
public class NullRole<P> implements Role<P> {

    @Override
    public boolean hasRole(P principal, ContainerRequestContext ctx) {
        return true;
    }
}
