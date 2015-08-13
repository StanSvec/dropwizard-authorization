package com.stansvec.dropwizard.auth.authorization

import com.stansvec.dropwizard.auth.AuthorizationConfiguration
import com.stansvec.dropwizard.auth.ProtectionPolicy
import com.stansvec.dropwizard.auth.TestAuthenticator
import com.stansvec.dropwizard.auth.TestUser
import com.stansvec.dropwizard.auth.roles.Admin
import io.dropwizard.testing.junit.ResourceTestRule
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory
import org.junit.Rule
import spock.lang.Specification

import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.xml.bind.DatatypeConverter

import static javax.ws.rs.client.Entity.entity
import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION
import static javax.ws.rs.core.Response.Status.OK
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED

/**
 * Tests for authentication and authorization.
 */
class AuthorizationWithEntityTest extends Specification {

    @Rule
    ResourceTestRule rule = ResourceTestRule
            .builder()
            .setTestContainerFactory(new GrizzlyWebTestContainerFactory())
            .addResource(new EntityResource())
            .addProvider(createConfiguration())
            .build();

    static AuthorizationConfiguration createConfiguration() {
        return new AuthorizationConfiguration.Builder<TestUser>()
                .setPolicy(ProtectionPolicy.PROTECT_ANNOTATED_ONLY)
                .addRole(new Admin())
                .setAuthentication(TestAuthenticator.AUTH_FACT)
                .build();
    }

    def "test authorization"() {
        expect:
        def response = getResponse(rule, resource, user)
        status.statusCode == response.getStatus()
        response.readEntity(String.class).contains(result)

        where:
        resource                    | user                 | status         | result
        "/entity/parameter/admin"   | TestUser.ADMIN       | OK             | "{\"result\" : \"value1,value2\"}"
        "/entity/parameter/admin"   | TestUser.GUEST       | UNAUTHORIZED   | "Unauthorized"
        "/entity/method/admin"      | TestUser.ADMIN       | OK             | "{\"result\" : \"value1,value2\"}"
        "/entity/method/admin"      | TestUser.GUEST       | UNAUTHORIZED   | "Unauthorized"
    }

    Response getResponse(ResourceTestRule resources, String resource, TestUser user) {
        return resources.getJerseyTest()
                .target(resource)
                .request()
                .header(AUTHORIZATION, user != null ? "Basic " + DatatypeConverter.printBase64Binary((user.name + ":pass").getBytes("UTF-8")) : "")
                .post(entity("{\"field1\" : \"value1\", \"field2\" : \"value2\"}", MediaType.APPLICATION_JSON_TYPE))
    }
}
