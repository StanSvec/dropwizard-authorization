package com.stansvec.dropwizard.auth;

/**
 * Created by turtles on 06/07/15.
 */
public class InvalidAuthorizationConfigurationException extends RuntimeException {

    public InvalidAuthorizationConfigurationException(String message) {
        super(message);
    }
}
