/*
 * This code is licensed under "The MIT License"
 * Copyright (c) 2015 by Stan Svec
 *
 * Please see the included 'LICENSE.txt' file for the full text of the license.
 */

package com.stansvec.dropwizard.auth;

import com.google.common.collect.ImmutableList;
import com.stansvec.dropwizard.auth.exp.ExpressionEngine;
import io.dropwizard.auth.AuthFactory;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.jersey.server.internal.inject.ParamInjectionResolver;
import org.glassfish.jersey.server.spi.internal.ValueFactoryProvider;
import org.glassfish.jersey.server.wadl.processor.OptionsMethodProcessor;

import javax.inject.Singleton;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;

/**
 * Configures JAX-RS container to use request filter to perform authentication / authorization
 * and to use injection resolver to inject principal instance into resource method.
 *
 * Various checks are performed during registration to validate correct usage of this extension
 * like that all defined roles are registered, annotation are used according to documented rules, etc.
 *
 * @author Stan Svec
 */
@Provider
class AuthConfigurationImpl<P> extends AuthConfiguration {

    private final ProtectionPolicy protectionPolicy;

    private final AuthFactory<?, ? extends P> authFactory;

    private final Authorization<? super P> authorization;

    private final ExpressionEngine<? super P> expressionEngine;

    public AuthConfigurationImpl(ProtectionPolicy protectionPolicy,
                                 AuthFactory<?, ? extends P> authFactory,
                                 Authorization<? super P> authorization,
                                 ExpressionEngine<? super P> expressionEngine) {
        this.protectionPolicy = protectionPolicy;
        this.authFactory = authFactory;
        this.authorization = authorization;
        this.expressionEngine = expressionEngine;
    }

    @Override
    public void configure(ResourceInfo resInfo, FeatureContext context) {
        if (OptionsMethodProcessor.class.equals(resInfo.getResourceClass().getEnclosingClass())) { // maybe there could be a better solution..
            return;
        }

        Auth auth = resolveAuthAnnotation(
                ImmutableList.<AnnotatedElement>of(resInfo.getResourceClass(), resInfo.getResourceMethod()),
                resInfo.getResourceMethod().getParameters(),
                resInfo);

        if (auth != null) {
            checkRoles(auth, resInfo);
            checkExpression(auth, resInfo);
            context.register(new AuthFilter<>(authFactory, authorization, auth));
            expressionEngine.registerExpression(auth.check());
        }
    }

    private Auth resolveAuthAnnotation(List<AnnotatedElement> elements, Parameter[] params, ResourceInfo resInfo) {
        Auth auth = null;
        boolean ignore = false;
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
                if (!auth.required()) {
                    if (isAuthorizationUsed(auth)) {
                        throw exc("When authentication is optional then authorization cannot be set", resInfo);
                    }
                    boolean principalFound = false;
                    for (Parameter param : params) {
                        if (param.isAnnotationPresent(Principal.class)) {
                            principalFound = true;
                            break;
                        }
                    }
                    if (!principalFound) {
                        throw exc("Principal injection must be used if authentication is optional. Use @Principal annotation to inject principal into the method or set authentication as required", resInfo);
                    }
                }
            }
            if (!ignore) {
                ignore = e.isAnnotationPresent(NoAuth.class);
            }
        }

        if (auth == null && !ignore && (protectionPolicy == ProtectionPolicy.PROTECT_ALL)) {
            throw exc("Unprotected resource method found. Either allow unprotected methods with ProtectionPolicy.PROTECT_ANNOTATED_ONLY or use @NoAuth annotation", resInfo);
        }

        return (ignore ? null : auth);
    }

    private static boolean isAuthorizationUsed(Auth auth) {
        return !Arrays.equals(Auth.NO_ROLE, auth.roles()) || !Arrays.equals(Auth.NO_ROLE, auth.anyRole()) || !Auth.NO_EXP.equals(auth.check());
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

    private void checkExpression(Auth auth, ResourceInfo resInfo) {
        if ((expressionEngine == ExpressionEngine.NULL) && !Auth.NO_EXP.equals(auth.check())) {
            throw exc("An expression engine must be set to use expression in @Auth annotation. Check method supportExpressions(..) on AuthorizationConfiguration.Builder", resInfo);
        }
    }

    private static InvalidAuthConfigException exc(String msg, ResourceInfo resInfo) {
        return new InvalidAuthConfigException(msg + ", resource info: " + resInfo);
    }

    @Singleton
    public static class PrincipalType<T> {

        private final Class<T> type;

        public PrincipalType(Class<T> type) {
            this.type = type;
        }

        public Class<T> getType() {
            return type;
        }
    }

    public static class PrincipalInjectionResolver extends ParamInjectionResolver<Principal> {
        public PrincipalInjectionResolver() {
            super(PrincipalProviderFactory.class);
        }
    }

    @Override
    protected void configure() {
        bind(new PrincipalType<>(authFactory.getGeneratedClass())).to(PrincipalType.class);
        bind(PrincipalProviderFactory.class).to(ValueFactoryProvider.class).in(Singleton.class);
        bind(PrincipalInjectionResolver.class).to(new TypeLiteral<InjectionResolver<Principal>>() {}).in(Singleton.class);
    }
}
