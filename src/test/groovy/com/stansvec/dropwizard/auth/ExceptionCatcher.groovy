package com.stansvec.dropwizard.auth

import javax.ws.rs.container.DynamicFeature
import javax.ws.rs.container.ResourceInfo
import javax.ws.rs.core.FeatureContext

/**
 * Catching {@link InvalidAuthorizationConfigurationException} thrown during authorization configuration checking.
 */
class ExceptionCatcher implements DynamicFeature {

    final AuthorizationConfiguration authConfig

    InvalidAuthorizationConfigurationException exception

    ExceptionCatcher(AuthorizationConfiguration authConfig) {
        this.authConfig = authConfig
    }

    @Override
    void configure(ResourceInfo resourceInfo, FeatureContext context) {
        try {
            authConfig.configure(resourceInfo, context)
        } catch (InvalidAuthorizationConfigurationException e) {
            exception = e
        }
    }
}
