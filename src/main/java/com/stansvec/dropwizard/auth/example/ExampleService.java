package com.stansvec.dropwizard.auth.example;

import com.stansvec.dropwizard.auth.ConditionalAuthProvider;
import com.stansvec.dropwizard.auth.MvelExpressionEvaluation;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.auth.oauth.OAuthProvider;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

public class ExampleService extends Service<com.stansvec.dropwizard.auth.example.ExampleConfiguration> {

    public static void main(String[] args) throws Exception {
        new ExampleService().run(new String[]{"server"});
    }

    @Override
    public void initialize(Bootstrap<com.stansvec.dropwizard.auth.example.ExampleConfiguration> bootstrap) {
        bootstrap.setName("Conditional authorization example");
    }

    @Override
    public void run(com.stansvec.dropwizard.auth.example.ExampleConfiguration configuration, Environment environment) throws Exception {
        environment.addProvider(new ConditionalAuthProvider<com.stansvec.dropwizard.auth.example.ExampleAuthInfo>(
                new OAuthProvider<com.stansvec.dropwizard.auth.example.ExampleAuthInfo>(new com.stansvec.dropwizard.auth.example.ExampleAuthenticator(), "realm"),
                new MvelExpressionEvaluation<com.stansvec.dropwizard.auth.example.ExampleAuthInfo>(new ExampleVariableProvider())));
        environment.addResource(new com.stansvec.dropwizard.auth.example.ExampleResource());
    }
}
