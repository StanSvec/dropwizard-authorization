package com.stansvec.dropwizard.auth.example;

import com.stansvec.dropwizard.auth.Auth;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/auth/example")
@Produces(MediaType.TEXT_PLAIN)
public class ExampleResource {

    @GET
    @Path("/exp/success")
    public String exp(@Auth(check = "roles('role1') and user('stansvec')") ExampleAuthInfo info) {
        return "authorized";
    }

    @GET
    @Path("/manual/success")
    public String manual(@Auth ExampleAuthInfo info) {
        if (!info.getRoles().contains("role1") || !"stansvec".equals(info.getUser())) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }

        return "authorized";
    }

    @GET
    @Path("/exp/unauthorized")
    public String expUnauthorized(@Auth(check = "groups('admin')") ExampleAuthInfo info) {
        return "authorized";
    }

    @GET
    @Path("/exp/user/{user}/profile")
    public String expProfileOwnerOnly(@Auth(check = "owner()") ExampleAuthInfo info) {
        return "user profile";
    }

    @GET
    @Path("/manual/user/{user}/profile")
    public String manualProfileOwnerOnly(@Auth ExampleAuthInfo info, @Context UriInfo uriInfo) {
        if (!uriInfo.getPath().matches(String.format(".*user/%s($|/.*)", info.getUser()))) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }

        return "user profile";
    }
}
