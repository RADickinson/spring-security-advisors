package org.radickins.ssa.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service("securedService")
class SecuredServiceImpl implements SecuredService {
  @Override
  public List<SecuredVO> filterSecuredVOs(final List<SecuredVO> securedVOs) {
    return securedVOs;
  }
}
