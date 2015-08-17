/*
 * This code is licensed under "The MIT License"
 * Copyright (c) 2015 by Stan Svec
 *
 * Please see the included 'LICENSE.txt' file for the full text of the license.
 */

package com.stansvec.dropwizard.auth.authorization

import com.stansvec.dropwizard.auth.Auth
import com.stansvec.dropwizard.auth.CustomEntity
import com.stansvec.dropwizard.auth.Principal
import com.stansvec.dropwizard.auth.TestUser
import com.stansvec.dropwizard.auth.roles.Admin
import org.hamcrest.CoreMatchers

import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces

import static com.stansvec.dropwizard.auth.TestUser.ADMIN
import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertThat

/**
 * Protected resource using custom entity as one of the parameters.
 */
@Path("/entity")
@Consumes("application/json")
@Produces("application/json")
class EntityResource {

    @POST
    @Path("/principalInjected")
    @Auth(roles = Admin.class)
    String authOnParameter(@Principal TestUser user, CustomEntity entity) {
        assertThat(user.name, CoreMatchers.is(ADMIN.name))
        return String.format("{\"result\" : \"%s,%s\"}", entity.field1, entity.field2)
    }

    @POST
    @Path("/injectedOptional")
    @Auth(required = false)
    String optional(@Principal TestUser user, CustomEntity entity) {
        assertNotNull(entity)
        return String.format("{\"user\" : \"%s\"}", user?.name)
    }

    @POST
    @Path("/principalNotInjected")
    @Auth(roles = Admin.class)
    String authOnMethod(CustomEntity entity) {
        return String.format("{\"result\" : \"%s,%s\"}", entity.field1, entity.field2)
    }
}
