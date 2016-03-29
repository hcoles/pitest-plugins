package org.pitest.plugins.randomtests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.pitest.classinfo.ClassName;
import org.pitest.classpath.CodeSource;
import org.pitest.coverage.ClassLine;
import org.pitest.coverage.CoverageDatabase;
import org.pitest.coverage.TestInfo;
import org.pitest.functional.F;
import org.pitest.functional.FCollection;
import org.pitest.functional.FunctionalList;
import org.pitest.functional.Option;
import org.pitest.mutationtest.build.TestPrioritiser;
import org.pitest.mutationtest.config.PluginServices;
import org.pitest.mutationtest.engine.Location;
import org.pitest.mutationtest.engine.MethodName;
import org.pitest.mutationtest.engine.MutationDetails;
import org.pitest.mutationtest.engine.MutationIdentifier;
import org.pitest.plugin.ToolClasspathPlugin;

import java.util.List;
import java.util.Properties;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RandomTestPrioritiserFactoryTest {

    private final RandomTestPrioritiserFactory testee = new RandomTestPrioritiserFactory();

    @Mock
    private Properties props;

    @Mock
    private CodeSource code;

    @Mock
    private CoverageDatabase coverage;

    private static F<ToolClasspathPlugin, Class<?>> toClass() {
        return new F<ToolClasspathPlugin, Class<?>>() {
            public Class<?> apply(ToolClasspathPlugin a) {
                return a.getClass();
            }

        };
    }

    @Test
    public void shouldDeclareServiceInterface() {
        Iterable<? extends ToolClasspathPlugin> plugins = PluginServices.makeForContextLoader()
                .findToolClasspathPlugins();
        FunctionalList<Class<?>> pluginClasses = FCollection.map(plugins, toClass());
        assertThat(pluginClasses).contains(RandomTestPrioritiserFactory.class);
    }

    @Test
    public void shouldAssignTestsForRelevantLineToGeneratedMutations() {
        final List<TestInfo> expected = java.util.Collections.singletonList(test("a"));
        when(this.coverage.getTestsForClassLine(any(ClassLine.class))).thenReturn(expected);
        TestPrioritiser prioritiser = testee.makeTestPrioritiser(props, code, coverage);
        final List<TestInfo> actual = prioritiser.assignTests(makeMutation("foo"));
        assertEquals(expected, actual);
    }

    private TestInfo test(String name) {
        return new TestInfo("footest", name, 0, Option.<ClassName>none(), 0);
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
