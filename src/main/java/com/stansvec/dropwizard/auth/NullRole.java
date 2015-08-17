package com.stansvec.dropwizard.auth;

import javax.ws.rs.container.ContainerRequestContext;

/**
 * Null object for {@link Role}. Every principal has this roles. This is default authorization used when authorization is not required.
 *
 * @author Stan Svec
 */
public class NullRole<P> implements Role<P> {

    @Override
    public boolean hasRole(P principal, ContainerRequestContext ctx) {
        return true;
    }
}
