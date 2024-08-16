package org.radickins.ssa.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

import javax.inject.Named;

@Component
@Named("securityPermissionEvaluator")
final class CustomPermissionEvaluator implements PermissionEvaluator, Serializable {

  private static final Logger logger = LoggerFactory.getLogger(CustomPermissionEvaluator.class);

  private static final long serialVersionUID = -8484684438040342756L;


  @Override
  public boolean hasPermission(final Authentication authentication,
                               final Object targetDomainObject,
                               final Object permission) {

    if (targetDomainObject instanceof Filterable) {
      final Filterable filterable = (Filterable) targetDomainObject;
      if (filterable.shouldFilter()) {
        filterable.setFiltered();
      }
    }
    logger.debug("Evaluating permission: {} on target: {} for authentication: {}", permission, targetDomainObject, authentication);
    // Expensive custom security operations
    return true;
  }

  @Override
  public boolean hasPermission(final Authentication authentication,
                               final Serializable targetId,
                               final String targetType,
                               final Object permission) {

    logger.debug("Evaluating permission: {} on target: {}/{} for authentication: {}", permission, targetId, targetType, authentication);
    // Expensive custom security operations
    return true;
  }

}
