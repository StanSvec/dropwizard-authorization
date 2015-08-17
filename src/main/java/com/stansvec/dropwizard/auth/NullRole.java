/*
 * This code is licensed under "The MIT License"
 * Copyright (c) 2015 by Stan Svec
 *
 * Please see the included 'LICENSE.txt' file for the full text of the license.
 */

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
