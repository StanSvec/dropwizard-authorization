package com.stansvec.dropwizard.auth;

import io.dropwizard.auth.AuthFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;

/**
 * Created by turtles on 20/06/15.
 */
public class AuthorizationFactory<C, P> extends AuthFactory<C, P> {

    private final AuthFactory<C, P> authFactory;

    private final Authorization<? super P> authorization;

    private final Auth auth;

    private final ContainerRequestContext containerRequest;

    public AuthorizationFactory(AuthFactory<C, P> authFactory, Authorization<? super P> authorization) {
        this(authFactory, authorization, null, null);
    }

    private AuthorizationFactory(AuthFactory<C, P> authFactory, Authorization<? super P> authorization, Auth auth, ContainerRequestContext containerRequest) {
        super(null);
        this.authFactory = authFactory;
        this.auth = auth;
        this.containerRequest = containerRequest;
        this.authorization = authorization;
    }

    @Override
    @Context
    public void setRequest(HttpServletRequest request) {
        authFactory.setRequest(request);
    }

    public AuthorizationFactory<C, P> clone(Auth auth, ContainerRequestContext contReq) {
        return new AuthorizationFactory<>(authFactory.clone(auth.required()), authorization, auth, contReq);
    }

    @Override
    public AuthFactory<C, P> clone(boolean required) {
        throw new AssertionError("This is not supposed to be called");
    }

    @Override
    public Class<P> getGeneratedClass() {
        return authFactory.getGeneratedClass();
    }

    @Override
    public P provide() {
        ContainerRequestContext cr = (containerRequest != null) ? containerRequest : getContainerRequest();
        P principal = authFactory.provide();
        authorization.authorize(auth, principal, cr);
        return principal;
    }
}
