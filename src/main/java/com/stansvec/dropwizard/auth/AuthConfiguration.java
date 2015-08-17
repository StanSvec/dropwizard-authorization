package com.stansvec.dropwizard.auth;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;
import com.stansvec.dropwizard.auth.exp.ExpressionEngine;
import io.dropwizard.auth.AuthFactory;
import io.dropwizard.auth.DefaultUnauthorizedHandler;
import io.dropwizard.auth.UnauthorizedHandler;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.ws.rs.container.DynamicFeature;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Defines single type for both {@link AbstractBinder} and {@link DynamicFeature} which is used for registration of authorization configuration.
 * Contains builder class for building {@link AuthConfiguration} instance.
 *
 * @author Stan Svec
 */
public abstract class AuthConfiguration extends AbstractBinder implements DynamicFeature {

    public static class Builder<U> {

        private ProtectionPolicy protectionPolicy;

        private ClassToInstanceMap<Role<U>> roles = MutableClassToInstanceMap.create();

        private ExpressionEngine<? super U> expressionEngine = ExpressionEngine.NULL;

        private UnauthorizedHandler unauthorizedHandler = new DefaultUnauthorizedHandler();

        public InterBuilder setPolicy(ProtectionPolicy protectionPolicy) {
            checkNotNull(protectionPolicy);
            this.protectionPolicy = protectionPolicy;
            return new InterBuilder();
        }

        public class InterBuilder {

            @SuppressWarnings("unchecked")
            public InterBuilder addRole(Role<U> role) {
                roles.put((Class<? extends Role<U>>) role.getClass(), role);
                return this;
            }

            public InterBuilder supportExpressions(ExpressionEngine<? super U> expEngine) {
                checkNotNull(expressionEngine);
                expressionEngine = expEngine;
                return this;
            }

            public InterBuilder setUnauthorizedHandler(UnauthorizedHandler unAuthHandler) {
                checkNotNull(unAuthHandler);
                unauthorizedHandler = unAuthHandler;
                return this;
            }

            public <T> FinalBuilder<T> setAuthentication(AuthFactory<T, U> authFactory) {
                checkNotNull(authFactory);
                return new FinalBuilder<>(authFactory);
            }
        }

        public class FinalBuilder<T> {

            private AuthFactory<T, U> authFactory;

            FinalBuilder(AuthFactory<T, U> authFactory) {
                this.authFactory = authFactory;
            }

            public AuthConfiguration build() {
                Authorization<U> authorization = new Authorization<>(roles, expressionEngine, unauthorizedHandler);
                return new AuthConfigurationImpl<>(protectionPolicy, authFactory, authorization, expressionEngine);
            }
        }
    }
}
