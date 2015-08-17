/*
 * This code is licensed under "The MIT License"
 * Copyright (c) 2015 by Stan Svec
 *
 * Please see the included 'LICENSE.txt' file for the full text of the license.
 */

package com.stansvec.dropwizard.auth;

/**
 * This exception is thrown if configuration of the authentication / authorization is invalid.
 *
 * @author Stan Svec
 */
public class InvalidAuthConfigException extends RuntimeException {

    public InvalidAuthConfigException(String message) {
        super(message);
    }
}
