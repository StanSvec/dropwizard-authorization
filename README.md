[![Build Status](https://travis-ci.org/StanSvec/dropwizard-authorization.svg?branch=master)](https://travis-ci.org/StanSvec/dropwizard-authorization)

dropwizard-authorization
===================

The library enhances dropwizard-auth with authorization capability.

Programmer writes boolean expression within the Auth annotation. An user is authorized only when the expression evaluates to true.
E.g. <code>@Auth(check = "group('admin')")</code> allows access only to users which are members of admin group.
The functions like group(String) are defined by library users. See the package com.stansvec.dropwizard.auth.example to know how to use the library.
The library uses MVEL as default expression language.