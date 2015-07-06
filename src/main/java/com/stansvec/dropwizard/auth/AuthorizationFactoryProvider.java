package com.stansvec.dropwizard.auth;

import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.internal.inject.AbstractValueFactoryProvider;
import org.glassfish.jersey.server.internal.inject.MultivaluedParameterExtractorProvider;
import org.glassfish.jersey.server.internal.inject.ParamInjectionResolver;
import org.glassfish.jersey.server.model.Parameter;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by turtles on 20/06/15.
 */
@Singleton
public class AuthorizationFactoryProvider<C, P> extends AbstractValueFactoryProvider {

    private final AuthorizationFactory<C, P> factory;

    @Inject
    public AuthorizationFactoryProvider(
            final MultivaluedParameterExtractorProvider extractorProvider, final AuthorizationFactory<C, P> factory, final ServiceLocator injector) {
        super(extractorProvider, injector, Parameter.Source.UNKNOWN);
        this.factory = factory;
    }

    @Override
    protected Factory<?> createValueFactory(Parameter parameter) {
        final Class<?> classType = parameter.getRawType();
        final Auth auth = parameter.getAnnotation(Auth.class);

        if (auth != null && classType.isAssignableFrom(factory.getGeneratedClass())) {
            return factory.clone(auth, null);
        } else {
            return null;
        }
    }

    public static class AuthInjectionResolver extends ParamInjectionResolver<Auth> {
        public AuthInjectionResolver() {
            super(AuthorizationFactoryProvider.class);
        }
    }

}