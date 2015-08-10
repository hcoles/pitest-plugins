package org.pitest.plugins.alltests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.pitest.classinfo.ClassName;
import org.pitest.classpath.CodeSource;
import org.pitest.coverage.CoverageDatabase;
import org.pitest.coverage.TestInfo;
import org.pitest.functional.F;
import org.pitest.functional.FCollection;
import org.pitest.mutationtest.build.TestPrioritiser;
import org.pitest.mutationtest.build.TestPrioritiserFactory;

/**
 * Runs the entire test suite against each mutation.
 */
public class AllTestsPrioritiserFactory implements TestPrioritiserFactory {

  public String description() {
    return "All tests prioritiser";
  }

  public TestPrioritiser makeTestPrioritiser(Properties properties, CodeSource code,
      CoverageDatabase coverage) {
    Set<TestInfo> tis = new HashSet<TestInfo>();
    FCollection.flatMapTo(code.getCodeUnderTestNames(), classToTests(coverage), tis);
    
    List<TestInfo> sorted = new ArrayList<TestInfo>(tis);
    
    // sort to a consistent order
    Collections.sort(sorted,  nameComparator());
    
    return new AllTestsPrioritiser(sorted);
  }

  private Comparator<TestInfo> nameComparator() {
    return new Comparator<TestInfo>() {
      @Override
      public int compare(TestInfo arg0, TestInfo arg1) {
        return arg0.getName().compareTo(arg1.getName());
      }
      
    };
  }

  private static F<ClassName, Iterable<TestInfo>> classToTests(final CoverageDatabase coverage) {
    return new F<ClassName, Iterable<TestInfo>>() {
      @Override
      public Iterable<TestInfo> apply(ClassName clazz) {
        return coverage.getTestsForClass(clazz);
      }
      
    };
  }

}
