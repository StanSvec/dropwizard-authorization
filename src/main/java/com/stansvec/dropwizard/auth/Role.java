/*
 * This code is licensed under "The MIT License"
 * Copyright (c) 2015 by Stan Svec
 *
 * Please see the included 'LICENSE.txt' file for the full text of the license.
 */

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
