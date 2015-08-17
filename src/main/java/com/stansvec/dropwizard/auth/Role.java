package com.stansvec.dropwizard.auth;

import javax.ws.rs.container.ContainerRequestContext;

/**
 * Defines authorization role.
 *
 * @author Stan Svec
 */
public interface Role<P> {

    boolean hasRole(P principal, ContainerRequestContext ctx);
}
