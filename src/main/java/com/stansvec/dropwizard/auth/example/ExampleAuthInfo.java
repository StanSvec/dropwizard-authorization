package com.stansvec.dropwizard.auth.example;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

public class ExampleAuthInfo {

    public String getUser() {
        return "stansvec";
    }

    public Set<String> getGroups() {
        return ImmutableSet.of("user", "privileged");
    }

    public Set<String> getRoles() {
        return ImmutableSet.of("role1", "role2");
    }
}
