package org.pitest.plugins.alltests;

import java.util.List;

import org.pitest.coverage.TestInfo;
import org.pitest.mutationtest.build.TestPrioritiser;
import org.pitest.mutationtest.engine.MutationDetails;

/**
 * Runs the entire test suite against each mutation.
 */
public class AllTestsPrioritiser implements TestPrioritiser {
  
  private final List<TestInfo> allTests;
  
  public AllTestsPrioritiser(List<TestInfo> tis) {
    this.allTests = tis;
  }

  public List<TestInfo> assignTests(MutationDetails mutation) {
      return allTests;
  }
  

}
