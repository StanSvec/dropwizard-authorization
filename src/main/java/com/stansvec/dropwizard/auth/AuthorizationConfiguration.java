package com.stansvec.dropwizard.auth;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.MutableClassToInstanceMap;
import io.dropwizard.auth.AuthFactory;
import io.dropwizard.auth.DefaultUnauthorizedHandler;
import io.dropwizard.auth.UnauthorizedHandler;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.spi.internal.ValueFactoryProvider;
import org.glassfish.jersey.server.wadl.processor.OptionsMethodProcessor;

import javax.inject.Singleton;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by turtles on 20/06/15.
 */
@Provider
public class AuthorizationConfiguration<T, U> extends AbstractBinder implements DynamicFeature {

    private final AuthPolicy authPolicy;

    private final AuthorizationFactory<T, U> authorizationFactory;

    private final Authorization<? super U> authorization;

    public AuthorizationConfiguration(AuthPolicy authPolicy, AuthorizationFactory<T, U> authFactory, Authorization<? super U> authorization) {
        this.authPolicy = authPolicy;
        this.authorization = authorization;
        this.authorizationFactory = authFactory;
    }

    @Override
    public void configure(ResourceInfo resInfo, FeatureContext context) {
        if (OptionsMethodProcessor.class.equals(resInfo.getResourceClass().getEnclosingClass())) { // maybe there could be a better solution..
            return;
        }

        Auth auth = resolveAuthAnnotation(
                ImmutableList.<AnnotatedElement>builder()
                        .add(resInfo.getResourceClass())
                        .add(resInfo.getResourceMethod())
                        .addAll(Arrays.asList(resInfo.getResourceMethod().getParameters()))
                        .build(), resInfo);

        if (auth != null) {
            if (!Auth.NO_EXP.equals(auth.check())) { // TODO
                throw exc("Expression engine must be set to use expression in @Auth annotation. Check method supportExpressions(..) on AuthorizationConfiguration.Builder", resInfo);
            }
            checkRoles(auth, resInfo);
            context.register(new AuthorizationFilter(authorizationFactory, auth));
        }
    }

    private Auth resolveAuthAnnotation(List<AnnotatedElement> elements, ResourceInfo resInfo) {
        Auth auth = null;
        boolean ignore = false;
        boolean onParam = false;
        for (AnnotatedElement e : elements) {
            if (e.isAnnotationPresent(Auth.class)) {
                if (ignore) {
                    throw exc("@Auth cannot be used if @NoAuth is used on higher level (target)", resInfo);
                }
                if (auth != null) {
                    throw exc("@Auth annotation cannot be combined on different levels (targets)", resInfo);
                }
                if (e.isAnnotationPresent(NoAuth.class)) {
                    throw exc("Both @Auth and @NoAuth cannot be combined on same level (target)", resInfo);
                }
                auth = e.getAnnotation(Auth.class);
                if (Parameter.class.isAssignableFrom(e.getClass())) {
                    if (!auth.required() && (!Arrays.equals(Auth.NO_ROLE, auth.roles()) || !Arrays.equals(Auth.NO_ROLE, auth.anyRole()) || !Auth.NO_EXP.equals(auth.check()))) {
                        throw exc("When authentication is optional then authorization cannot be set", resInfo);
                    }
                    onParam = true;
                } else if (!auth.required()) {
                    throw exc("Optional authentication/authorization is allowed only on parameter level", resInfo);
                }
            }
            if (!ignore) {
                ignore = e.isAnnotationPresent(NoAuth.class);
            }
        }

        if (auth == null && !ignore && (authPolicy == AuthPolicy.PROTECT_ALL)) {
            throw exc("Expression engine must be set to use expression in @Auth annotation. Check method supportExpressions(..) on AuthorizationConfiguration.Builder", resInfo);
        }

        return (ignore || onParam) ? null : auth;
    }

    private void checkRoles(Auth auth, ResourceInfo resInfo) {
        checkRoles(auth.roles(), resInfo);
        checkRoles(auth.anyRole(), resInfo);
    }

    private void checkRoles(Class<? extends Role>[] roles, ResourceInfo resInfo) {
        for (Class<? extends Role> role : roles) {
            if (!authorization.containRole(role)) {
                throw exc("@Auth annotation contains unregistered role: " + role, resInfo);
            }
        }
    }

    private static InvalidAuthorizationConfigurationException exc(String msg, ResourceInfo resInfo) {
        return new InvalidAuthorizationConfigurationException(msg + ", resource info: " + resInfo);
    }

    @Override
    protected void configure() {
        bind(authorizationFactory).to(AuthorizationFactory.class);
        bind(AuthorizationFactoryProvider.class).to(ValueFactoryProvider.class).in(Singleton.class);
        bind(AuthorizationFactoryProvider.AuthInjectionResolver.class).to(new TypeLiteral<InjectionResolver<Auth>>() {
        }).in(Singleton.class);
    }

    public static class Builder<U> {

        private AuthPolicy authPolicy;

        private ClassToInstanceMap<Role<U>> roles = MutableClassToInstanceMap.create();

        private UnauthorizedHandler unauthorizedHandler = new DefaultUnauthorizedHandler();

        public InterBuilder setAuthPolicy(AuthPolicy authPolicy) {
            checkNotNull(authPolicy);
            this.authPolicy = authPolicy;
            return new InterBuilder();
        }

        public class InterBuilder {

            @SuppressWarnings("unchecked")
            public InterBuilder addRole(Role<U> role) {
                roles.put((Class<? extends Role<U>>) role.getClass(), role);
                return this;
            }

            public InterBuilder setUnauthorizedHandler(UnauthorizedHandler unAuthHandler) {
                checkNotNull(unAuthHandler);
                unauthorizedHandler = unAuthHandler;
                return this;
            }

            public <T> FinalBuilder<T> setAuthentication(AuthFactory<T, U> authorizationFactory) {
                return new FinalBuilder<>(authorizationFactory);
            }
        }

        public class FinalBuilder<T> {

            private AuthFactory<T, U> authFactory;

            public FinalBuilder(AuthFactory<T, U> authFactory) {
                this.authFactory = authFactory;
            }

            public AuthorizationConfiguration<T, U> build() {
                Authorization<U> auth = new Authorization<>(roles, unauthorizedHandler);
                return new AuthorizationConfiguration<>(authPolicy, new AuthorizationFactory<>(authFactory, auth), auth);
            }
        }
    }
}
