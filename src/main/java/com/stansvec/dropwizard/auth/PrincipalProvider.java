/*
 * This code is licensed under "The MIT License"
 * Copyright (c) 2015 by Stan Svec
 *
 * Please see the included 'LICENSE.txt' file for the full text of the license.
 */

package com.stansvec.dropwizard.auth;

import org.glassfish.jersey.server.internal.inject.AbstractContainerRequestValueFactory;

/**
 * Provides principal to be injected into resource method.
 *
 * @author Stan Svec
 */
public class PrincipalProvider<P> extends AbstractContainerRequestValueFactory<P> {

    @Override
    @SuppressWarnings("unchecked")
    public P provide() {
        return (P) getContainerRequest().getProperty(PrincipalId.VALUE);
    }
}
