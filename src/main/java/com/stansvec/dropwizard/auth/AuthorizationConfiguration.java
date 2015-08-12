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
 * Created by turtles on 20/06/15.
 */

public abstract class AuthorizationConfiguration extends AbstractBinder implements DynamicFeature {

    public static class Builder<U> {

        private ProtectionPolicy protectionPolicy;

        private ClassToInstanceMap<Role<U>> roles = MutableClassToInstanceMap.create();

        private UnauthorizedHandler unauthorizedHandler = new DefaultUnauthorizedHandler();

        private ExpressionEngine<? super U> expressionEngine = ExpressionEngine.NULL;

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

            public InterBuilder setUnauthorizedHandler(UnauthorizedHandler unAuthHandler) {
                checkNotNull(unAuthHandler);
                unauthorizedHandler = unAuthHandler;
                return this;
            }

            public InterBuilder supportExpressions(ExpressionEngine<? super U> expEngine) {
                checkNotNull(expressionEngine);
                expressionEngine = expEngine;
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

            public AuthorizationConfiguration build() {
                Authorization<U> auth = new Authorization<>(roles, unauthorizedHandler, expressionEngine);
                return new AuthorizationConfigurationImpl<>(protectionPolicy, new AuthorizationFactory<>(authFactory, auth), auth, expressionEngine);
            }
        }
    }
}
