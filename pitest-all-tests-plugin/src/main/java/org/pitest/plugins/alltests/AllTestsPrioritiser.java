package org.pitest.plugins.alltests;

import org.pitest.coverage.TestInfo;
import org.pitest.mutationtest.build.TestPrioritiser;
import org.pitest.mutationtest.engine.MutationDetails;

import java.util.List;

/**
 * Runs the entire test suite against each mutation.
 */
public class AllTestsPrioritiser implements TestPrioritiser {

    private final List<TestInfo> allTests;

    public AllTestsPrioritiser(List<TestInfo> tis) {
        this.allTests = tis;
    }

    @Override
    public List<TestInfo> assignTests(MutationDetails mutation) {
        return allTests;
    }

}
