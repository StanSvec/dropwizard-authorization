/*
 * This code is licensed under "The MIT License"
 * Copyright (c) 2015 by Stan Svec
 *
 * Please see the included 'LICENSE.txt' file for the full text of the license.
 */

package com.stansvec.dropwizard.auth

import org.glassfish.jersey.test.TestProperties
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

/**
 * Setting port number to system property which is supposed to be subsequently read by jersey.
 */
class SetPortRule implements TestRule {

    final int port

    SetPortRule(int port) {
        this.port = port
    }

    @Override
    Statement apply(Statement base, Description description) {
        return new Statement() {

            @Override
            void evaluate() throws Throwable {
                System.setProperty(TestProperties.CONTAINER_PORT, port + "")
                base.evaluate()
            }
        }
    }
}
