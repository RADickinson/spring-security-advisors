package org.radickins.ssa.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.stereotype.Component;

import java.util.Collection;

import javax.inject.Inject;

@Component("expressionHandler")
class CustomExpressionHandler extends DefaultMethodSecurityExpressionHandler {

  private static final Logger logger = LoggerFactory.getLogger(CustomExpressionHandler.class);

  @Inject
  CustomExpressionHandler(final PermissionEvaluator permissionEvaluator) {
    super();
    this.setPermissionEvaluator(permissionEvaluator);
    logger.debug("CustomExpressionHandler constructor.");
  }

  @Override
  public final Object filter(final Object filterTarget,
                             final Expression filterExpression,
                             final EvaluationContext ctx) {
    logger.debug("Filtering target: {}", filterTarget);
    if (filterTarget instanceof Collection) {
      checkCollection((Collection<?>) filterTarget);
    }
    return super.filter(filterTarget, filterExpression, ctx);
  }


  private void checkCollection(final Collection<?> filterTarget) {
    final boolean alreadyFiltered = filterTarget.stream()
        .filter(element -> element instanceof Filterable)
        .map(Filterable.class::cast)
        .anyMatch(Filterable::isFiltered);

    if (alreadyFiltered) {
      throw new RuntimeException("Collection already filtered.");
    }
  }
}
