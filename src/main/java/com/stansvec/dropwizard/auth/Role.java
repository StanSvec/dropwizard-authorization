package com.stansvec.dropwizard.auth;

import javax.ws.rs.container.ContainerRequestContext;

/**
 * Created by turtles on 21/06/15.
 */
public interface Role<P> {

    boolean hasRole(P principal, ContainerRequestContext ctx);
}
