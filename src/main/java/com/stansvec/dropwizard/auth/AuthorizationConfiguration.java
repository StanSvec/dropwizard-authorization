package com.stansvec.dropwizard.auth;

import io.dropwizard.auth.*;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.spi.internal.ValueFactoryProvider;

import javax.inject.Singleton;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.Parameter;

/**
 * Created by turtles on 20/06/15.
 */
@Provider
public class AuthorizationConfiguration<T, U> extends AbstractBinder implements DynamicFeature {

    private final AuthorizationFactory<T, U> authorizationFactory;

    public AuthorizationConfiguration(AuthFactory<T, U> authFactory, Authorization<? super U> authorization) {
        this.authorizationFactory = new AuthorizationFactory<>(authFactory, authorization);
    }

    @Override
    public void configure(ResourceInfo resourceInfo, FeatureContext context) {
        Auth auth = resourceInfo.getResourceMethod().getAnnotation(Auth.class);
        if (auth == null) {
            auth = resourceInfo.getResourceClass().getAnnotation(Auth.class);
            if (auth == null) {
                checkParametersAnnotation(resourceInfo);
                return;
            }
        }
        check(resourceInfo, auth);
        context.register(new AuthorizationFilter(authorizationFactory, auth));
    }

    private static void checkParametersAnnotation(ResourceInfo resInfo) {
        boolean found = false;
        for (Parameter param : resInfo.getResourceMethod().getParameters()) {
            Auth auth = param.getAnnotation(Auth.class);
            if (auth == null) {
                continue;
            }
            if (found) {
                throw new InvalidAuthorizationConfigurationException("Only one parameter can be annotated with @Auth");
            }
            if (!auth.required() && (!Auth.NO_ROLE.equals(auth.roles()) || !Auth.NO_ROLE.equals(auth.anyRole()) || !Auth.NO_EXP.equals(auth.exp()))) {
                throw new InvalidAuthorizationConfigurationException("Authorization cannot be set if authentication is not required");
            }
            found = true;
        }
    }

    private static void check(ResourceInfo resInfo, Auth auth) {
        for (Parameter param : resInfo.getResourceMethod().getParameters()) {
            if (param.isAnnotationPresent(Auth.class)) {
                throw new InvalidAuthorizationConfigurationException("@Auth annotation cannot be combined on parameter+method or parameter+class levels");
            }
        }
        if (!auth.required()) {
            throw new InvalidAuthorizationConfigurationException("Optional authentication/authorization is allowed only on parameter level");
        }
        if (!Auth.NO_EXP.equals(auth.exp())) {
            // TODO check expression engine
        }
    }

    @Override
    protected void configure() {
        bind(authorizationFactory).to(AuthorizationFactory.class);
        bind(AuthorizationFactoryProvider.class).to(ValueFactoryProvider.class).in(Singleton.class);
        bind(AuthorizationFactoryProvider.AuthInjectionResolver.class).to(new TypeLiteral<InjectionResolver<Auth>>() {
        }).in(Singleton.class);
    }
}
