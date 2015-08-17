/*
 * This code is licensed under "The MIT License"
 * Copyright (c) 2015 by Stan Svec
 *
 * Please see the included 'LICENSE.txt' file for the full text of the license.
 */

package com.stansvec.dropwizard.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to specify authentication requirement for accessing the resources.
 * Optionally the authorization rules can be set.
 *
 * @author Stan Svec
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Auth {

    String NO_EXP = "true";

    @SuppressWarnings("unchecked")
    Class<? extends Role>[] NO_ROLE = new Class[]{NullRole.class};

    Class<? extends Role>[] roles() default NullRole.class;

    Class<? extends Role>[] anyRole() default NullRole.class;

    String check() default NO_EXP;

    boolean required() default true;
}
