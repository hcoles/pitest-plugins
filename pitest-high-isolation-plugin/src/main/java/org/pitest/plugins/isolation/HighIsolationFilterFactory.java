package org.pitest.plugins.isolation;

import org.pitest.classpath.CodeSource;
import org.pitest.mutationtest.filter.MutationFilter;
import org.pitest.mutationtest.filter.MutationFilterFactory;

import java.util.Properties;

public class HighIsolationFilterFactory implements MutationFilterFactory {

  public String description() {
    return "High isolation filter";
  }

  public MutationFilter createFilter(Properties properties, CodeSource source, int maxMutationsPerClass) {
    return new HighIsolationFilter();
  }
}
