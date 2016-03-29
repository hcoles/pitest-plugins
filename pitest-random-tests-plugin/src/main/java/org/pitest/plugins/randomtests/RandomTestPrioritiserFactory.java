package org.pitest.plugins.randomtests;

import org.pitest.classpath.CodeSource;
import org.pitest.coverage.CoverageDatabase;
import org.pitest.mutationtest.build.TestPrioritiser;
import org.pitest.mutationtest.build.TestPrioritiserFactory;

import java.util.Properties;

/**
 * @author marcinMarcin
 */
public class RandomTestPrioritiserFactory implements TestPrioritiserFactory {
    @Override
    public TestPrioritiser makeTestPrioritiser(Properties props, CodeSource code, CoverageDatabase coverage) {
        return new RandomTestPrioritiser(coverage);
    }

    @Override
    public String description() {
        return "Random test prioritiser";
    }

}