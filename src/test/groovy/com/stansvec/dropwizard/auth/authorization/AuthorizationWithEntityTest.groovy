/*
 * This code is licensed under "The MIT License"
 * Copyright (c) 2015 by Stan Svec
 *
 * Please see the included 'LICENSE.txt' file for the full text of the license.
 */

package com.stansvec.dropwizard.auth.authorization

import com.stansvec.dropwizard.auth.AuthConfiguration
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

    static AuthConfiguration createConfiguration() {
        return new AuthConfiguration.Builder<TestUser>()
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
        resource                        | user                 | status         | result
        "/entity/principalInjected"     | TestUser.ADMIN       | OK             | "{\"result\" : \"value1,value2\"}"
        "/entity/principalInjected"     | TestUser.GUEST       | UNAUTHORIZED   | "Unauthorized"
        "/entity/principalNotInjected"  | TestUser.ADMIN       | OK             | "{\"result\" : \"value1,value2\"}"
        "/entity/principalNotInjected"  | TestUser.GUEST       | UNAUTHORIZED   | "Unauthorized"
        "/entity/injectedOptional"      | TestUser.ADMIN       | OK             | "{\"user\" : \"admin\"}"
        "/entity/injectedOptional"      | null                 | OK             | "{\"user\" : \"null\"}"
    }

    Response getResponse(ResourceTestRule resources, String resource, TestUser user) {
        return resources.getJerseyTest()
                .target(resource)
                .request()
                .header(AUTHORIZATION, user != null ? "Basic " + DatatypeConverter.printBase64Binary((user.name + ":pass").getBytes("UTF-8")) : "")
                .post(entity("{\"field1\" : \"value1\", \"field2\" : \"value2\"}", MediaType.APPLICATION_JSON_TYPE))
    }
}
