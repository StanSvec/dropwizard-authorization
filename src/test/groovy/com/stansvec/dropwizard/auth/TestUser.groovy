/*
 * This code is licensed under "The MIT License"
 * Copyright (c) 2015 by Stan Svec
 *
 * Please see the included 'LICENSE.txt' file for the full text of the license.
 */

package com.stansvec.dropwizard.auth

import groovy.transform.ToString

import static java.util.EnumSet.of

/**
 * Test principal.
 */
@ToString
class TestUser {

    static def ADMIN = new TestUser("admin", of(Role.ADMIN))

    static def SUPER_USER = new TestUser("super_user", of(Role.SUPER_USER))

    static def EDITOR = new TestUser("editor", of(Role.EDITOR))

    static def GUEST = new TestUser("guest", of(Role.GUEST))

    static def ADMIN_SUPER = new TestUser("admin_super", of(Role.ADMIN, Role.SUPER_USER))

    static def ADMIN_EDITOR = new TestUser("admin_editor", of(Role.ADMIN, Role.EDITOR))

    static def ADMIN_SUPER_EDITOR = new TestUser("admin_super_editor", of(Role.ADMIN, Role.SUPER_USER, Role.EDITOR))

    static def ADMIN_SUPER_GUEST = new TestUser("admin_super_guest", of(Role.ADMIN, Role.SUPER_USER, Role.GUEST))

    static def USERS = [ADMIN, SUPER_USER, EDITOR, GUEST, ADMIN_SUPER, ADMIN_EDITOR, ADMIN_SUPER_EDITOR, ADMIN_SUPER_GUEST]

    final String name;

    final Set<Role> roles;

    public TestUser(String name, Set<Role> roles) {
        this.name = name;
        this.roles = roles
    }

    boolean hasRole(Role role) {
        return roles.contains(role)
    }

    enum Role {
        ADMIN, SUPER_USER, EDITOR, GUEST
    }
}
