package com.stansvec.dropwizard.auth.config

import com.stansvec.dropwizard.auth.*
import com.stansvec.dropwizard.auth.roles.Admin
import io.dropwizard.testing.junit.ResourceTestRule
import org.glassfish.jersey.test.TestProperties
import org.junit.Rule
import org.junit.rules.RuleChain
import spock.lang.Specification

/**
 * Abstract class for testing authorization configuration.
 */
abstract class AbstractConfigurationTest extends Specification {

    ExceptionCatcher catcherAll = new ExceptionCatcher(createConfiguration(AuthPolicy.PROTECT_ALL))

    ExceptionCatcher catcherAnnotated = new ExceptionCatcher(createConfiguration(AuthPolicy.PROTECT_ANNOTATED_ONLY))

    ResourceTestRule ruleAll = ResourceTestRule
            .builder()
            .addResource(resource())
            .addProvider(catcherAll)
            .build();

    ResourceTestRule ruleAnnotated = ResourceTestRule
            .builder()
            .addResource(resource())
            .addProvider(catcherAnnotated)
            .build();
    @Rule
    RuleChain ruleChain = RuleChain
            .outerRule(new SetPortRule(TestProperties.DEFAULT_CONTAINER_PORT))
            .around(ruleAll)
            .around(new SetPortRule(TestProperties.DEFAULT_CONTAINER_PORT + 1))
            .around(ruleAnnotated);

    static AuthorizationConfiguration createConfiguration(AuthPolicy authPolicy) {
        return new AuthorizationConfiguration.Builder<TestUser>()
                .setAuthPolicy(authPolicy)
                .addRole(new Admin())
                .setAuthentication(TestAuthenticator.AUTH_FACT)
                .build();
    }

    abstract Object resource()

    abstract boolean expectExceptionOnProtectAnnotated()

    abstract boolean expectExceptionOnProtectAll()

    def "check configuration"() {
        expect:
        success(catcherAnnotated, expectExceptionOnProtectAnnotated())
        success(catcherAll, expectExceptionOnProtectAll())
    }

    boolean success(ExceptionCatcher catcher, boolean expect) {
        expect ? (catcher.exception != null && InvalidAuthorizationConfigurationException.class.equals(catcher.exception.class)) : (catcher.exception == null)
    }
}
