package org.radickins.ssa.security;

public interface Filterable {

  boolean shouldFilter();

  boolean isFiltered();

  void setFiltered();

}
