package com.stansvec.dropwizard.auth;

/**
 * Test principal.
 */
class TestUser {

    private final String name;

    public TestUser(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
