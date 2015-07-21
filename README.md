[![Build Status](https://travis-ci.org/StanSvec/dropwizard-authorization.svg?branch=master)](https://travis-ci.org/StanSvec/dropwizard-authorization)

## Authorization for Dropwizard 0.8.x


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

### @Auth annotation
`@Auth` annotation is used for protecting resources. Unlike Dropwizard `@Auth` annotation this annotation supports defining roles and expressions for authorization purposes. The annotation can be used on type (class), method or parameter level.

Annotation elements:
* `required`: The element has same semantics as `requried` element in Dropwizard `@Auth` annotation. If set to false then authentication is optional, no other annotation elements can be set (authorization is forbidden) and the annotation can be used on parameter level only.
* `roles`: Defines role(s) required for a principal to access a resource.
* `anyRoles`: Define any roles required for a principal to access a resource.
* `check`: Contains boolean expression which needs to be evaluated as true to allow a principal access a resource.

`@Auth` usage examples:

```java
/**
 * {@link Auth} annotation is used on type level therefore all resource methods defined in the class are protected.
 */
@Path("/protectedType")
@Auth(roles = Editor.class, anyRole = {Admin.class, Owner.class})
public class ProtectedTypeResource {
    
    @GET
    public String protectedGet() {
        return "principal must have role Editor and at least one role from roles Admin and Owner";
    }

    @POST
    public String protectedPost() {
        return "principal must have role Editor and at least one role from roles Admin and Owner";
    }
}
```
```java
/**
 * {@link Auth} annotation is used on method or parameter level to protect resource methods differently.
 */
@Path("/protectedMethods")
public class ProtectedMethodsResource {

    @GET
    @Auth(anyRole = {Admin.class, Owner.class})
    public String protectedGet() {
        return "principal must have at least one role from roles Admin and Owner";
    }

    @POST
    public String protectedPost(@Auth(roles = Editor.class) Principal principal) {
        return "principal must have role Editor";
    }
}
```

### @NoAuth annotation
This annotation is used for specifying unprotected resources. It can be used on type (class) and method level. When used on method level then the resource method is excluded from authentication and authorization defined by `@Auth` annotation on type level and thus makes the resource method unprotected. Another use case is related to `AuthPolicy#PROTECT_ALL` policy. When this policy is set then @NoAuth annotation can still be used both on type and method level to specify unprotected resources.

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