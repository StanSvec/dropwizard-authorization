package com.stansvec.dropwizard.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to inject authenticated principal objects into protected JAX-RS resource
 * methods when expression check is evaluated to true.
 *
 * @author Stan Svec
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER})
public @interface Auth {

    String NO_EXP = "true";

    @SuppressWarnings("unchecked")
    Class<? extends Role>[] NO_ROLE = new Class[]{NullRole.class};

    Class<? extends Role>[] roles() default NullRole.class;

    Class<? extends Role>[] anyRole() default NullRole.class;

    String check() default NO_EXP;

    boolean required() default true;
}
