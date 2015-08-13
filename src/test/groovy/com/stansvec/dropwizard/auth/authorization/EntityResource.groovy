package com.stansvec.dropwizard.auth.authorization

import com.stansvec.dropwizard.auth.Auth
import com.stansvec.dropwizard.auth.CustomEntity
import com.stansvec.dropwizard.auth.TestUser
import com.stansvec.dropwizard.auth.roles.Admin

import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces

/**
 * Protected resource using custom entity as one of the parameters.
 */
@Path("/entity")
@Consumes("application/json")
@Produces("application/json")
class EntityResource {

    @POST
    @Path("/parameter/admin")
    String authOnParameter(@Auth(roles = Admin.class) TestUser user, CustomEntity entity) {
        return String.format("{\"result\" : \"%s,%s\"}", entity.field1, entity.field2)
    }

    @POST
    @Path("/method/admin")
    @Auth(roles = Admin.class)
    String authOnMethod(CustomEntity entity) {
        return String.format("{\"result\" : \"%s,%s\"}", entity.field1, entity.field2)
    }
}
