package org.radickins.ssa.service;

import org.springframework.security.access.prepost.PostFilter;

import java.util.List;

public interface SecuredService {

  @PostFilter("hasPermission(filterObject, 'SecuredVO:read')")
  List<SecuredVO> filterSecuredVOs(List<SecuredVO> securedVOs);
}
