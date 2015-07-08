package com.stansvec.dropwizard.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to inject authenticated principal objects into protected JAX-RS resource
 * methods when expression exp is evaluated to true.
 *
 * @author Stan Svec
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER})
public @interface Auth {

    String NO_EXP = "";

    Class<? extends Role> NO_ROLE = NullRole.class;

    Class<? extends Role>[] roles() default NullRole.class;

    Class<? extends Role>[] anyRole() default NullRole.class;

    String exp() default NO_EXP;

    boolean required() default true;
}
