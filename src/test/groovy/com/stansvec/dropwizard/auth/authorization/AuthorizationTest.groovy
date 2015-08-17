package com.stansvec.dropwizard.auth.authorization

import com.stansvec.dropwizard.auth.*
import com.stansvec.dropwizard.auth.roles.Admin
import com.stansvec.dropwizard.auth.roles.Editor
import com.stansvec.dropwizard.auth.roles.Guest
import com.stansvec.dropwizard.auth.roles.SuperUser
import io.dropwizard.testing.junit.ResourceTestRule
import org.glassfish.jersey.test.TestProperties
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory
import org.junit.Rule
import org.junit.rules.RuleChain
import spock.lang.Specification

import javax.xml.bind.DatatypeConverter

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION
import static javax.ws.rs.core.Response.Status.NO_CONTENT
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED

/**
 * Tests for authentication and authorization.
 */
class AuthorizationTest extends Specification {

    ResourceTestRule ruleAll = ResourceTestRule
            .builder()
            .setTestContainerFactory(new GrizzlyWebTestContainerFactory())
            .addResource(new ProtectedTypeResource())
            .addResource(new ProtectedMethodsResource())
            .addResource(new UnprotectedTypeResource())
            .addProvider(createConfiguration(ProtectionPolicy.PROTECT_ALL))
            .build();

    ResourceTestRule ruleAnnotated = ResourceTestRule
            .builder()
            .setTestContainerFactory(new GrizzlyWebTestContainerFactory())
            .addResource(new ProtectedTypeResource())
            .addResource(new ProtectedAndUnannotatedMethodsResource())
            .addResource(new UnprotectedTypeResource())
            .addResource(new UnannotatedTypeResource())
            .addProvider(createConfiguration(ProtectionPolicy.PROTECT_ANNOTATED_ONLY))
            .build();
    @Rule
    RuleChain ruleChain = RuleChain
            .outerRule(new SetPortRule(TestProperties.DEFAULT_CONTAINER_PORT))
            .around(ruleAll)
            .around(new SetPortRule(TestProperties.DEFAULT_CONTAINER_PORT + 1))
            .around(ruleAnnotated);

    static AuthConfiguration createConfiguration(ProtectionPolicy authPolicy) {
        return new AuthConfiguration.Builder<TestUser>()
                .setPolicy(authPolicy)
                .addRole(new Admin())
                .addRole(new SuperUser())
                .addRole(new Editor())
                .addRole(new Guest())
                .setAuthentication(TestAuthenticator.AUTH_FACT)
                .build();
    }

    def "test authorization for both policies"() {
        expect:
        status.statusCode == getStatus(ruleAll, resource, user)
        status.statusCode == getStatus(ruleAnnotated, resource, user)

        where:
        resource                                            | user                          | status
        "/protectedType/admin"                              | TestUser.ADMIN                | NO_CONTENT
        "/protectedType/admin"                              | null                          | UNAUTHORIZED
        "/protectedType/admin"                              | TestUser.EDITOR               | UNAUTHORIZED
        "/protectedType/admin/injected"                     | TestUser.ADMIN                | NO_CONTENT
        "/protectedType/admin/injected"                     | TestUser.EDITOR               | UNAUTHORIZED
        "/protectedType/admin/injected"                     | null                          | UNAUTHORIZED
        "/protectedType/unprotected"                        | TestUser.EDITOR               | NO_CONTENT
        "/protectedType/unprotected"                        | null                          | NO_CONTENT
        "/protectedMethods/admin-and-editor"                | TestUser.ADMIN_EDITOR         | NO_CONTENT
        "/protectedMethods/admin-and-editor"                | TestUser.ADMIN                | UNAUTHORIZED
        "/protectedMethods/admin-and-editor"                | TestUser.EDITOR               | UNAUTHORIZED
        "/protectedMethods/admin-and-editor"                | null                          | UNAUTHORIZED
        "/protectedMethods/editor-or-guest"                 | TestUser.EDITOR               | NO_CONTENT
        "/protectedMethods/editor-or-guest"                 | TestUser.GUEST                | NO_CONTENT
        "/protectedMethods/editor-or-guest"                 | TestUser.ADMIN                | UNAUTHORIZED
        "/protectedMethods/editor-or-guest"                 | null                          | UNAUTHORIZED
        "/protectedMethods/admin-and-super+editor-or-guest" | TestUser.ADMIN_SUPER_EDITOR   | NO_CONTENT
        "/protectedMethods/admin-and-super+editor-or-guest" | TestUser.ADMIN_SUPER_GUEST    | NO_CONTENT
        "/protectedMethods/admin-and-super+editor-or-guest" | TestUser.ADMIN_SUPER          | UNAUTHORIZED
        "/protectedMethods/admin-and-super+editor-or-guest" | TestUser.ADMIN_EDITOR         | UNAUTHORIZED
        "/protectedMethods/admin-and-super+editor-or-guest" | null                          | UNAUTHORIZED
        "/protectedMethods/unprotected"                     | TestUser.ADMIN                | NO_CONTENT
        "/protectedMethods/unprotected"                     | null                          | NO_CONTENT
        "/unprotectedType/unprotected"                      | TestUser.ADMIN                | NO_CONTENT
        "/unprotectedType/unprotected"                      | null                          | NO_CONTENT
    }

    def "test authorization for policy annotated_only"() {
        expect:
        status.statusCode == getStatus(ruleAnnotated, resource, user)

        where:
        resource                            | user                  | status
        "/unannotatedType/unprotected"      | TestUser.ADMIN        | NO_CONTENT
        "/unannotatedType/unprotected"      | null                  | NO_CONTENT
        "/protectedMethods/unannotated"     | TestUser.EDITOR       | NO_CONTENT
        "/protectedMethods/unannotated"     | null                  | NO_CONTENT
    }

    int getStatus(ResourceTestRule resources, String resource, TestUser user) {
        return resources.getJerseyTest()
                .target(resource)
                .request()
                .header(AUTHORIZATION, user != null ? "Basic " + DatatypeConverter.printBase64Binary((user.name + ":pass").getBytes("UTF-8")) : "")
                .get()
                .getStatus()
    }
}
