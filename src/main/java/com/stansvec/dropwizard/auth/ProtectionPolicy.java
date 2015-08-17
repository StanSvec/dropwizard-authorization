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
