package org.pitest.plugins.randomtests;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pitest.classinfo.ClassByteArraySource;
import org.pitest.classinfo.ClassName;
import org.pitest.coverage.ClassLine;
import org.pitest.coverage.CoverageDatabase;
import org.pitest.coverage.TestInfo;
import org.pitest.functional.F;
import org.pitest.functional.FCollection;
import org.pitest.functional.Option;
import org.pitest.mutationtest.engine.Location;
import org.pitest.mutationtest.engine.MethodName;
import org.pitest.mutationtest.engine.MutationDetails;
import org.pitest.mutationtest.engine.MutationIdentifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * @author marcinMarcin
 */
public class RandomTestPrioritiserTest {

    private RandomTestPrioritiser testee;
    @Mock
    private CoverageDatabase coverage;
    @Mock
    private ClassByteArraySource source;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.testee = new RandomTestPrioritiser(this.coverage);
    }

    @Test
    public void shouldAssignAllTestsForClassWhenMutationInStaticInitialiser() {
        final List<TestInfo> expected = makeTestInfos("a");
        when(this.coverage.getTestsForClass(ClassName.fromString("foo"))).thenReturn(expected);
        final List<TestInfo> actual = this.testee
                .assignTests(makeMutation("<clinit>"));
        assertEquals(expected, actual);
    }

    @Test
    public void shouldPrioritiseTestsRandomly() {
        this.testee.setSeed(2);
        final List<String> expected = Arrays.asList("d", "b", "a", "c");
        final List<TestInfo> unorderedTests = makeTestInfos("a", "b", "c", "d");
        when(this.coverage.getTestsForClassLine(any(ClassLine.class))).thenReturn(
                unorderedTests);
        final List<TestInfo> actual = this.testee.assignTests(makeMutation("foo"));

        assertEquals(expected, FCollection.map(actual, toName()));
    }

    private F<TestInfo, String> toName() {
        return new F<TestInfo, String>() {
            @Override
            public String apply(TestInfo a) {
                return a.getName();
            }
        };
    }

    private List<TestInfo> makeTestInfos(final String... names) {
        return new ArrayList<TestInfo>(FCollection.map(Arrays.asList(names),
                nameToTestInfo()));
    }

    private F<String, TestInfo> nameToTestInfo() {
        return new F<String, TestInfo>() {
            @Override
            public TestInfo apply(final String name) {
                return new TestInfo("footest", name, 1, Option.<ClassName>none(), 0);
            }

        };
    }

    private MutationDetails makeMutation(final String method) {
        return new MutationDetails(aMutationId(method), "file", "desc", 1, 2);
    }

    private MutationIdentifier aMutationId(final String method) {
        return new MutationIdentifier(aLocation(method), 0, "foo");
    }

    private Location aLocation(final String method) {
        return new Location(ClassName.fromString("foo"), MethodName.fromString(method), "");
    }

}
