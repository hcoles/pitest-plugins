package org.pitest.plugins.isolation;

import org.pitest.functional.F;
import org.pitest.functional.FCollection;
import org.pitest.mutationtest.engine.MutationDetails;
import org.pitest.mutationtest.filter.MutationFilter;
import org.pitest.util.Log;

import java.util.Collection;
import java.util.logging.Logger;

/**
 * Simple filter that marks all mutations as requiring classloader
 * isolation.
 * <p/>
 * This will remove false positives due to poisoning of static
 * state within the JVM, but will run much slower and may
 * require additional permgen space to be configured in the JVM.
 */
public class HighIsolationFilter implements MutationFilter {
    protected static final boolean REQUIRES_ISOLATION = true;

    private final static Logger LOG = Log.getLogger();

    private static F<MutationDetails, MutationDetails> markForIsolation() {
        return new F<MutationDetails, MutationDetails>() {
            public MutationDetails apply(MutationDetails a) {
                return new MutationDetails(a.getId(), a.getFilename(),
                        a.getDescription(), a.getLineNumber(), a.getBlock(),
                        a.isInFinallyBlock(), REQUIRES_ISOLATION);
            }
        };
    }

    public Collection<MutationDetails> filter(
            Collection<MutationDetails> mutations) {
        LOG.info("Marking all mutations as requiring isolation");
        return FCollection.map(mutations, markForIsolation());
    }

}
