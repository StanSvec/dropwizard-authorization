package com.stansvec.dropwizard.auth;

import com.stansvec.dropwizard.auth.AuthConfigurationImpl.PrincipalType;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.internal.inject.AbstractValueFactoryProvider;
import org.glassfish.jersey.server.internal.inject.MultivaluedParameterExtractorProvider;
import org.glassfish.jersey.server.model.Parameter;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Creates {@link PrincipalProvider} for request.
 *
 * @author Stan Svec
 */
@Singleton
public class PrincipalProviderFactory<P> extends AbstractValueFactoryProvider {

    private final PrincipalType<P> principalType;

    @Inject
    public PrincipalProviderFactory(MultivaluedParameterExtractorProvider extractorProvider, ServiceLocator injector, PrincipalType<P> principalType) {
        super(extractorProvider, injector, Parameter.Source.UNKNOWN);
        this.principalType = principalType;
    }

    @Override
    protected Factory<?> createValueFactory(Parameter parameter) {
        Class<?> classType = parameter.getRawType();
        Principal principal = parameter.getAnnotation(Principal.class);

        if ((principal != null) && classType.isAssignableFrom(principalType.getType())) {
            return new PrincipalProvider<>();
        } else {
            return null;
        }
    }
}