package org.radickins.ssa.service;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.radickins.ssa.security.Filterable;

public class SecuredVO implements Filterable {

  private boolean filtered = false;

  private final long id;

  private final boolean shouldFilter;

  public SecuredVO(final long id, final boolean shouldFilter) {
    this.id = id;
    this.shouldFilter = shouldFilter;
  }

  @Override
  public boolean shouldFilter() {
    return shouldFilter;
  }

  @Override
  public boolean isFiltered() {
    return filtered;
  }

  @Override
  public void setFiltered() {
    this.filtered = true;
  }

  public long getId() {
    return id;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("id", id)
        .append("shouldFilter", shouldFilter)
        .append("filtered", filtered)
        .toString();
  }
}
