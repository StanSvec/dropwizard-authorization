package com.stansvec.dropwizard.auth.example;

import com.stansvec.dropwizard.auth.ConditionalAuthProvider;
import com.stansvec.dropwizard.auth.MvelExpressionEvaluation;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.auth.oauth.OAuthProvider;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

public class ExampleService extends Service<ExampleConfiguration> {

    public static void main(String[] args) throws Exception {
        new ExampleService().run(new String[]{"server"});
    }

    @Override
    public void initialize(Bootstrap<ExampleConfiguration> bootstrap) {
        bootstrap.setName("Conditional authorization example");
    }

    @Override
    public void run(ExampleConfiguration configuration, Environment environment) throws Exception {
        environment.addProvider(new ConditionalAuthProvider<ExampleAuthInfo>(
                new OAuthProvider<ExampleAuthInfo>(new ExampleAuthenticator(), "realm"),
                new MvelExpressionEvaluation<ExampleAuthInfo>(new ExampleVariableProvider())));
        environment.addResource(new ExampleResource());
    }
}
