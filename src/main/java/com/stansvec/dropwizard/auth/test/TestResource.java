package com.stansvec.dropwizard.auth.test;

import com.stansvec.dropwizard.auth.Auth;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * Created by turtles on 20/06/15.
 */
@Path("fufu")
public class TestResource {

    @GET
    public String paramLevel(@Auth User user) {
        return "fufik";
    }

    @GET
    @Path("tutu")
    @Auth(roles = Owner.class)
    public String methodLevel() {
        return "juzek";
    }
}
