[![Build Status](https://travis-ci.org/StanSvec/dropwizard-authorization.svg?branch=master)](https://travis-ci.org/StanSvec/dropwizard-authorization)

## Authorization for Dropwizard 0.8.x

```
For Dropwizard 0.8.x use 0.1.4 version from jCenter.
```

This extension uses custom `@Auth` annotation for principal authentication and authorization. There are several options how to define authorization rules:
* By defining roles. A principal needs to have roles specified by `@Auth` annotation elements to be granted access a resource.
* By defining expressions. Expression specified by `@Auth` annotation element must be evaluated as true in order to allow a principal access a resource.
* Roles and expressions can be combined.

### Defining roles
Roles are defined by implementing `Role` interface.
```java
public class Roles {

    public static class Admin implements Role<Principal> {

        @Override
        public boolean hasRole(Principal principal, ContainerRequestContext ctx) {
            return principal.getRoles().contains("admin");
        }
    }

    public static class Owner implements Role<Principal> {

        @Override
        public boolean hasRole(Principal principal, ContainerRequestContext ctx) {
            return ("/user/" + principal.getId()).equals(ctx.getUriInfo().getRequestUri().getPath());
        }
    }
}
```

### Using expressions
You need to implement `ExpressionEngine` interface or use [MVEL Expression Engine](https://github.com/StanSvec/dropwizard-authorization-mvel).

### @Auth annotation
`@Auth` annotation is used for protecting resources. Unlike Dropwizard `@Auth` annotation this annotation supports defining roles and expressions for authorization purposes. The annotation can be used on type (class) or method.

Annotation elements:
* `required`: The element has same semantics as `required` element in Dropwizard `@Auth` annotation. If set to false then authentication is optional. In that case no other annotation elements can be set (authorization is forbidden) and principal instance must be injected into resource method by using `@Principal` annotation.
* `roles`: Defines role(s) required for a principal to access a resource.
* `anyRoles`: Define any roles required for a principal to access a resource.
* `check`: Contains boolean expression which needs to be evaluated as true to allow a principal access a resource.

### @Principal annotation
This annotation can be used on parameter level for injecting principal instance into resource method. This may be convenient if the resource method still needs principal information for other than authentication / authorization purposes.

`@Auth` and `@Principal` usage examples:

```java
/**
 * {@link Auth} annotation is used on type level therefore all resource methods defined in the class are protected.
 */
@Path("/protectedType")
@Auth(roles = Editor.class, anyRole = {Admin.class, Owner.class})
public class ProtectedTypeResource {
    
    @GET
    public String protectedGet() {
        return "principal must have role Editor and at least one of roles Admin and Owner";
    }

    @POST
    public String protectedPost() {
        return "principal must have role Editor and at least one of roles Admin and Owner";
    }
}
```
```java
/**
 * {@link Auth} annotation is used on method level + example of principal injection.
 */
@Path("/protectedMethods")
public class ProtectedMethodsResource {

    @GET
    @Auth(anyRole = {Admin.class, Owner.class})
    public String protectedGet() {
        return "principal must have at least one of roles Admin and Owner";
    }

    @POST
    @Auth(roles = Editor.class)
    public String protectedPost(@Principal Principal principal) {
        return "principal must have role Editor; principal instance is injected into this method";
    }

    @GET
    @Path("/optional")
    @Auth(required = false)
    public String optionalAuthentication(@Principal Principal principal) {
        return "no authentication required; if authentication is not used then principal instance is null";
    }
}
```

### @NoAuth annotation
This annotation is used for specifying unprotected resources. It can be used on type (class) and method level. When used on method level then the resource method is excluded from authentication and authorization defined by `@Auth` annotation on type level and thus makes the resource method unprotected. Another use case is related to `ProtectionPolicy#PROTECT_ALL` policy. When this policy is set then `@NoAuth` annotation can still be used both on type and method level to specify unprotected resources.

`@NoAuth` usage examples:
```java
/**
 * {@link NoAuth} annotation is used to specify unprotected resource method.
 */
@Path("/protectedTypeWithUnprotectedMethod")
@Auth(roles = Admin.class)
public class ProtectedTypeWithUnprotectedMethodResource {

    @GET
    @NoAuth
    public String unprotectedGet() {
        return "anyone can access this resource method";
    }

    @POST
    public String protectedPost() {
        return "principal must have role Admin to access this resource method";
    }
}
```

### Configuration and Dropwizard integration
For using this extension protection policy, custom roles and authentication must be set with `AuthorizationConfiguration.Builder` class. Dropwizard-authentication module is used for the authentication.
```java
AuthConfiguration authConfig = new AuthConfiguration.Builder<Principal>()
                .setPolicy(ProtectionPolicy.PROTECT_ANNOTATED_ONLY)
                .addRole(new Admin())
                .addRole(new SuperUser())
                .addRole(new Editor())
                .addRole(new Guest())
                .supportExpressions(expressionEngine) // <- optional, only if you want to use expressions in @Auth#check()
                .setAuthentication(new BasicAuthFactory<>(new TestAuthenticator(), "realm", Principal.class))
                .build();
environment.jersey().register(authConfig);
```
### Protection policy
There are two protection policies available:
* `ProtectionPolicy.PROTECT_ANNOTATED_ONLY`: Behaves in the same manner as default Dropwizard behaviour. Only resources annotated with `@Auth` annotation are protected.
* `ProtectionPolicy.PROTECT_ALL`: All resources must be protected. If unprotected resource is found then `InvalidAuthorizationConfigurationException` is thrown on Dropwizard startup. However `@NoAuth` annotation can still be used for making some of the resources unprotected. The main purpose of this policy is to ensure that no resource remains unprotected by mistake.