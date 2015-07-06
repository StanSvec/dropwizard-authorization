package com.stansvec.dropwizard.auth.test;

import com.google.common.base.Optional;
import com.stansvec.dropwizard.auth.Authorization;
import com.stansvec.dropwizard.auth.AuthorizationConfiguration;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicAuthFactory;
import io.dropwizard.auth.basic.BasicCredentials;
import io.dropwizard.setup.Environment;

/**
 * Created by turtles on 20/06/15.
 */
public class TestService extends Application<TestConfig> {

    public static void main(String[] args) throws Exception {
        new TestService().run("server");
    }

    @Override
    public void run(TestConfig configuration, Environment environment) throws Exception {
        environment.jersey().register(new TestResource());
        environment.jersey().register(new AuthorizationConfiguration(new BasicAuthFactory(new Authenticator<BasicCredentials, User>() {
            @Override
            public Optional<User> authenticate(BasicCredentials credentials) throws AuthenticationException {
                return Optional.of(new User("Ljflj"));
            }
        }, "realm", User.class), new Authorization(null, null)));
    }
}
