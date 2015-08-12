package com.stansvec.dropwizard.auth;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.reflect.Invokable;
import com.google.common.reflect.Parameter;
import com.stansvec.dropwizard.auth.exp.ExpressionEngine;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.jersey.server.spi.internal.ValueFactoryProvider;
import org.glassfish.jersey.server.wadl.processor.OptionsMethodProcessor;

import javax.inject.Singleton;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.List;

/**
 * Created by turtles on 25/07/15.
 */
@Provider
class AuthorizationConfigurationImpl<T, U> extends AuthorizationConfiguration {

    private final ProtectionPolicy protectionPolicy;

    private final AuthorizationFactory<T, U> authorizationFactory;

    private final Authorization<? super U> authorization;

    private final ExpressionEngine<? super U> expressionEngine;

    public AuthorizationConfigurationImpl(ProtectionPolicy protectionPolicy,
                                          AuthorizationFactory<T, U> authFactory,
                                          Authorization<? super U> authorization,
                                          ExpressionEngine<? super U> expressionEngine) {
        this.protectionPolicy = protectionPolicy;
        this.authorization = authorization;
        this.authorizationFactory = authFactory;
        this.expressionEngine = expressionEngine;
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
                        .addAll(Invokable.from(resInfo.getResourceMethod()).getParameters())
                        .build(),
                resInfo);

        if (auth != null) {
            checkRoles(auth, resInfo);
            checkExpression(auth, resInfo);
            context.register(new AuthorizationFilter(authorizationFactory, auth));
            expressionEngine.registerExpression(auth.check());
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

        if (auth == null && !ignore && (protectionPolicy == ProtectionPolicy.PROTECT_ALL)) {
            throw exc("Unprotected resource method found. Either allow unprotected methods with ProtectionPolicy.PROTECT_ANNOTATED_ONLY or use @NoAuth annotation", resInfo);
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

    private void checkExpression(Auth auth, ResourceInfo resInfo) {
        if (!Auth.NO_EXP.equals(auth.check()) && (expressionEngine == ExpressionEngine.NULL)) {
            throw exc("An expression engine must be set to use expression in @Auth annotation. Check method supportExpressions(..) on AuthorizationConfiguration.Builder", resInfo);
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
}
