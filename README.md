# Spring Security Advisor - bug reproducer

## Methods annotated with @PostFilter are processed twice by PostFilterAuthorizationMethodInterceptor

### Bug description

Found in version: **5.8.13**

Fix required in: **5.8.x** (required to enable migration to Spring Security 6).

While migrating from `@EnableGlobalMethodSecurity(prePostEnabled = true)` to `@EnableMethodSecurity` we have noticed the
methods annotated with `@PostFilter` are processed twice by the `PostFilterAuthorizationMethodInterceptor`.

We use a custom `PermissionEvaluator` and custom `MethodSecurityExpressionHandler` to evaluate `hasPermission`
expressions used with various prePost security annotations, for example:

```
  @PreAuthorize("hasPermission(#id, 'SecuredVO', 'SecuredVO:read')")
  @PostFilter("hasPermission(filterObject, 'SecuredVO:read')")
  List<SecuredVO> findByScopingId(final long scopingId);
```

The implementation of the `PermissionEvaluator` and `MethodSecurityExpressionHandler` is relatively operationally
expensive and filtered objects are modified in some cases (from custom types) as well as removed from standard array /
collection / stream types (using the `DefaultMethodSecurityExpressionHandler`). Running the filter twice for each
operation annotated with `@PostFilter` leads to application errors and also significantly reduces performance.

We need this issue to be fixed in the **5.8.x** version to enable us to complete migration steps towards upgrade to
Spring Security 6.

### Steps to reproduce

This repository contains sample code and configuration used to reproduce the issue in a much simplified state from the
original application. The issue can be reproduced simply by running the `SecuredServiceTest` which results in
a `RuntimeException` to be thrown stating `java.lang.RuntimeException: Collection already filtered.`. The components of
the test are:

- The configuration classes in `org.radickins.ssa` to component scan and `@EnableMethodSecurity`
- The minimal custom classes in `org.radickins.ssa.security` implementing the `PermissionEvaluator`
  and `MethodSecurityExpressionHandler`
- An implementation of a secured Service in `org.radickins.ssa.service` with a simple API annotated with `@PostFilter`

Given the `SecuredService` API that is annotated with `@PostFilter` is entirely self-contained in a simple
implementation, we expect the result to be filtered only once by the Spring Security framework. The permission evaluator
marks each object that should be filtered as having been filtered, and the expression handler raises an exception if it
detects the `filterTarget` has already been filtered. If working correctly, the expectation is the test should pass as
filtering only occurs once.

### Investigation

I have not attempted to fix the issue, but I believe the cause to be due to the bean registration of
the `PostFilterAuthorizationMethodInterceptor` bean. The `PrePostMethodSecurityConfiguration` configuration declares the
[postFilterAuthorizationMethodInterceptor](https://github.com/spring-projects/spring-security/blob/5.8.x/config/src/main/java/org/springframework/security/config/annotation/method/configuration/PrePostMethodSecurityConfiguration.java#L103)
bean as an `Advisor` type, and as you can see the other interceptors for `PreFilter`, `PreAuthorize`,
and `PostAuthorize` are each declared as `MethodInterceptor` types.

All of these interceptors are registered again as `Advisor` beans using
the [MethodSecurityAdvisorRegistrar](https://github.com/spring-projects/spring-security/blob/5.8.x/config/src/main/java/org/springframework/security/config/annotation/method/configuration/MethodSecurityAdvisorRegistrar.java#L29)

When the secured proxy is built, methods annotated with `@PostFilter` are assigned any `Advisor` beans required to
process the AOP security proxy invocation, and the `PostFilterAuthorizationMethodInterceptor` is added twice (as shown
in the image below).

![Screenshot of debugging a breakpoint in the CustomExpressionHandler](https://github.com/RADickinson/spring-security-advisors/blob/main/src/test/resources/AdvisorDebug.png)

I believe the fix is to simply register the `PostFilterAuthorizationMethodInterceptor` as the `MethodInterceptor` type
in the `PrePostMethodSecurityConfiguration` config.
