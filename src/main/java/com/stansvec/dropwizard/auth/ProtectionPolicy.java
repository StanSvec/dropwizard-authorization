/*
 * This code is licensed under "The MIT License"
 * Copyright (c) 2015 by Stan Svec
 *
 * Please see the included 'LICENSE.txt' file for the full text of the license.
 */

package com.stansvec.dropwizard.auth;

/**
 * Specifies how resources should be protected.
 *
 * @author Stan Svec
 */
public enum ProtectionPolicy {

    /**
     * Only resources annotated with {@link Auth} annotation are protected.
     */
    PROTECT_ANNOTATED_ONLY,

    /**
     * Every resource must be protected unless annotated with {@link NoAuth} annotation.
     */
    PROTECT_ALL
}
