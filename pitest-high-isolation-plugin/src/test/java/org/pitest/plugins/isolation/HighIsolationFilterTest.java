package org.pitest.plugins.isolation;

import org.fest.assertions.core.Condition;
import org.junit.Test;
import org.pitest.classinfo.ClassName;
import org.pitest.mutationtest.engine.Location;
import org.pitest.mutationtest.engine.MethodName;
import org.pitest.mutationtest.engine.MutationDetails;
import org.pitest.mutationtest.engine.MutationIdentifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

public class HighIsolationFilterTest {

    private final HighIsolationFilter testee = new HighIsolationFilter();

    @Test
    public void shouldMarkAllMutantsAsRequiringIsolation() {
        List<MutationDetails> mutations = new ArrayList<MutationDetails>();
        mutations.add(aMutationDetail());
        mutations.add(aMutationDetail());
        Collection<MutationDetails> actual = testee.filter(mutations);

        assertThat(actual).doNotHave(noIsolationMarker());
    }

    private Condition<? super MutationDetails> noIsolationMarker() {
        return new Condition<MutationDetails>() {
            @Override
            public boolean matches(MutationDetails value) {
                return !value.mayPoisonJVM();
            }
        };
    }

    private MutationDetails aMutationDetail() {
        return new MutationDetails(aMutationId(), "file", "desc", 1, 2);
    }

    private MutationIdentifier aMutationId() {
        return new MutationIdentifier(aLocation(), 0, "foo");
    }

    private Location aLocation() {
        return new Location(ClassName.fromString("foo"), MethodName.fromString("bar"), "");
    }

}
