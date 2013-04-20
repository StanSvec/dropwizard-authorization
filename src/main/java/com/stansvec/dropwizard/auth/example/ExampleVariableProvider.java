package com.stansvec.dropwizard.auth.example;

import com.google.common.collect.ImmutableMap;
import com.stansvec.dropwizard.auth.MvelVariableProvider;
import com.sun.jersey.api.core.HttpContext;
import org.mvel2.MVEL;
import org.mvel2.integration.VariableResolverFactory;
import org.mvel2.integration.impl.MapVariableResolverFactory;

public class ExampleVariableProvider implements MvelVariableProvider<com.stansvec.dropwizard.auth.example.ExampleAuthInfo> {

    @Override
    public VariableResolverFactory createCommonVariables() {
        VariableResolverFactory functionFactory = new MapVariableResolverFactory();
        MVEL.eval("def user(userParam) { usr == userParam; };", functionFactory);
        MVEL.eval("def groups(group) { gps contains group; };", functionFactory);
        MVEL.eval("def roles(role) { rls contains role; };", functionFactory);
        MVEL.eval("def owner() { path.matches('.*user/' + usr + '($|/.*)'); };", functionFactory);
        return functionFactory;
    }

    @Override
    public VariableResolverFactory createPerRequestVariables(ExampleAuthInfo res, HttpContext httpContext) {
        return new MapVariableResolverFactory(
                ImmutableMap.<String, Object>of(
                        "usr", res.getUser(),
                        "gps", res.getGroups(),
                        "rls", res.getRoles(),
                        "path", httpContext.getUriInfo().getPath()));
    }
}
