package org.radickins.ssa;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.radickins.ssa.service.SecuredService;
import org.radickins.ssa.service.SecuredVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringConfiguration.class})
public class SecuredServiceTest {

  private final Logger logger = LoggerFactory.getLogger(SecuredServiceTest.class);

  @Inject
  SecuredService securedService;

  @Test
  @WithMockUser("test")
  public final void testPostFilter() {
    final List<SecuredVO> input = List.of(new SecuredVO(1, true), new SecuredVO(2, false));
    logger.debug("input: {}", input);
    final List<SecuredVO> output = securedService.filterSecuredVOs(new ArrayList<>(input));
    logger.debug("output: {}", output);
  }

}
