package org.radickins.ssa;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.inject.Inject;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringConfiguration.class})
public class AppContextTest {

  private final Logger logger = LoggerFactory.getLogger(AppContextTest.class);

  @Inject
  ApplicationContext applicationContext;

  @BeforeEach
  public final void setup() {

    logger.debug("********************");
    final String[] beans = applicationContext.getBeanDefinitionNames();
    for (String o : beans) {
      logger.debug("BEAN = {}", o);
      logger.debug("\tType = {}", applicationContext.getType(o));
      final String[] aliases = applicationContext.getAliases(o);
      for (String a : aliases) {
        logger.debug("\tAliased as: {}", a);
      }
    }

    logger.debug("*** Number of Beans = {} ***", applicationContext.getBeanDefinitionCount());
  }

  @Test
  public final void testLoadContext() {
    logger.debug("Spring context loaded and tests running.");
  }

}
