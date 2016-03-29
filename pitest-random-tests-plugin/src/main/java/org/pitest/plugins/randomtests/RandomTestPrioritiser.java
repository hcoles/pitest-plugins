package org.pitest.plugins.randomtests;

import org.pitest.coverage.CoverageDatabase;
import org.pitest.coverage.TestInfo;
import org.pitest.functional.FCollection;
import org.pitest.functional.prelude.Prelude;
import org.pitest.mutationtest.build.TestPrioritiser;
import org.pitest.mutationtest.engine.MutationDetails;
import org.pitest.util.Log;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Runs the test suite in random order
 *
 * @author marcinMarcin
 */
public class RandomTestPrioritiser implements TestPrioritiser {

    private static final Logger LOG = Log.getLogger();
    private static final Random RANDOM = new Random();

    private final CoverageDatabase coverage;

    public RandomTestPrioritiser(CoverageDatabase coverage) {
        this.coverage = coverage;
    }

    @Override
    public List<TestInfo> assignTests(MutationDetails mutation) {
        return prioritizeTests(pickTests(mutation));
    }

    private Collection<TestInfo> pickTests(MutationDetails mutation) {
        if (!mutation.isInStaticInitializer()) {
            return this.coverage.getTestsForClassLine(mutation.getClassLine());
        } else {
            LOG.warning("Using untargetted tests");
            return this.coverage.getTestsForClass(mutation.getClassName());
        }
    }

    private List<TestInfo> prioritizeTests(Collection<TestInfo> testsForMutant) {
        final List<TestInfo> sortedTis = FCollection.map(testsForMutant, Prelude.id(TestInfo.class));
        Collections.shuffle(sortedTis, RANDOM);
        return sortedTis;
    }

    public void setSeed(long seed) {
        RANDOM.setSeed(seed);
    }
}
