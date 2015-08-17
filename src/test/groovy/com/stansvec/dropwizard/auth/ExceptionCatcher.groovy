package com.stansvec.dropwizard.auth

import javax.ws.rs.container.DynamicFeature
import javax.ws.rs.container.ResourceInfo
import javax.ws.rs.core.FeatureContext

/**
 * Catching {@link InvalidAuthConfigException} thrown during authorization configuration checking.
 */
class ExceptionCatcher implements DynamicFeature {

    final AuthConfiguration authConfig

    InvalidAuthConfigException exception

    ExceptionCatcher(AuthConfiguration authConfig) {
        this.authConfig = authConfig
    }

    @Override
    void configure(ResourceInfo resourceInfo, FeatureContext context) {
        try {
            authConfig.configure(resourceInfo, context)
        } catch (InvalidAuthConfigException e) {
            exception = e
        }
    }
}
