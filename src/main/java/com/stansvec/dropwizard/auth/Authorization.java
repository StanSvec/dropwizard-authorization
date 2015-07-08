package com.stansvec.dropwizard.auth;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.ImmutableClassToInstanceMap;
import io.dropwizard.auth.UnauthorizedHandler;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;

/**
 * Created by turtles on 21/06/15.
 */
public class Authorization<P> {

    private final ClassToInstanceMap<Role<P>> allRoles;

    private final UnauthorizedHandler unauthorizedHandler;

    public Authorization(ClassToInstanceMap<Role<P>> roles, UnauthorizedHandler unauthorizedHandler) {
        this.allRoles = ImmutableClassToInstanceMap.<Role<P>>builder().put(NullRole.class, new NullRole<>()).putAll(roles).build();
        this.unauthorizedHandler = unauthorizedHandler;
    }

    public boolean containRole(Class<? extends Role> role) {
        return allRoles.containsKey(role);
    }

    public void authorize(Auth auth, P principal, ContainerRequestContext ctx) {
        if (!checkRoles(auth.roles(), principal, ctx, false) || !checkRoles(auth.anyRole(), principal, ctx, true) || checkExpression(auth, principal, ctx)) {
            throw new WebApplicationException(unauthorizedHandler.buildResponse("prefix", "realm")); // TODO remove literals
        }
    }

    private boolean checkRoles(Class<? extends Role>[] roles, P principal, ContainerRequestContext ctx, boolean any) {
        boolean authorized = false;
        for (Class<? extends Role> r : roles) {
            if (allRoles.get(r).hasRole(principal, ctx)) {
                authorized = true;
                if (any) {
                    return true;
                }
            } else if (!any) {
                return false;
            }
        }

        return authorized;
    }

    private boolean checkExpression(Auth auth, P principal, ContainerRequestContext ctx) {
        return true; // TODO
    }
}
