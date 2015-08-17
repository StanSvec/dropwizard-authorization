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
