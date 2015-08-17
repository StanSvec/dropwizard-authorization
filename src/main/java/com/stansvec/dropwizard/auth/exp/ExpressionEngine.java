/*
 * This code is licensed under "The MIT License"
 * Copyright (c) 2015 by Stan Svec
 *
 * Please see the included 'LICENSE.txt' file for the full text of the license.
 */

package com.stansvec.dropwizard.auth.exp;

import javax.ws.rs.container.ContainerRequestContext;

/**
 * Evaluates authorization expressions.
 *
 * @author Stan Svec
 */
public interface ExpressionEngine<P> {

    ExpressionEngine<Object> NULL = new Null();

    void registerExpression(String expression);

    boolean evaluate(String expression, P principal, ContainerRequestContext ctx);

    class Null implements ExpressionEngine<Object> {

        @Override
        public void registerExpression(String expression) {}

        @Override
        public boolean evaluate(String expression, Object principal, ContainerRequestContext ctx) {
            return true;
        }
    }
}
