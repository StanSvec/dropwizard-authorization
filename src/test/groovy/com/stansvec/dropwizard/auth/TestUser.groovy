package com.stansvec.dropwizard.auth

import groovy.transform.ToString

import static java.util.EnumSet.of

/**
 * Test principal.
 */
@ToString
class TestUser {

    static def ADMIN = new TestUser("admin", of(TestUser.Role.ADMIN))

    static def SUPER_USER = new TestUser("super_user", of(TestUser.Role.SUPER_USER))

    static def EDITOR = new TestUser("editor", of(TestUser.Role.EDITOR))

    static def GUEST = new TestUser("guest", of(TestUser.Role.GUEST))

    static def ADMIN_SUPER = new TestUser("admin_super", of(TestUser.Role.ADMIN, TestUser.Role.SUPER_USER))

    static def ADMIN_EDITOR = new TestUser("admin_editor", of(TestUser.Role.ADMIN, TestUser.Role.EDITOR))

    static def ADMIN_SUPER_EDITOR = new TestUser("admin_super_editor", of(TestUser.Role.ADMIN, TestUser.Role.SUPER_USER, TestUser.Role.EDITOR))

    static def ADMIN_SUPER_GUEST = new TestUser("admin_super_guest", of(TestUser.Role.ADMIN, TestUser.Role.SUPER_USER, TestUser.Role.GUEST))

    static def USERS = [ADMIN, SUPER_USER, EDITOR, GUEST, ADMIN_SUPER, ADMIN_EDITOR, ADMIN_SUPER_EDITOR, ADMIN_SUPER_GUEST]

    final String name;

    final Set<TestUser.Role> roles;

    public TestUser(String name, Set<TestUser.Role> roles) {
        this.name = name;
        this.roles = roles
    }

    boolean hasRole(TestUser.Role role) {
        return roles.contains(role)
    }

    enum Role {
        ADMIN, SUPER_USER, EDITOR, GUEST
    }
}
