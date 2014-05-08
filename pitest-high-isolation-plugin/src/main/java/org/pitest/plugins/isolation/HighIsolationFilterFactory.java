package org.pitest.plugins.isolation;

import org.pitest.classpath.CodeSource;
import org.pitest.mutationtest.filter.MutationFilter;
import org.pitest.mutationtest.filter.MutationFilterFactory;

public class HighIsolationFilterFactory implements MutationFilterFactory {

  public String description() {
    return "High isolation filter";
  }

  public MutationFilter createFilter(CodeSource source, int maxMutationsPerClass) {
    return new HighIsolationFilter();
  }

}
