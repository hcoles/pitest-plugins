package org.pitest.plugins.alltests;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.pitest.classinfo.ClassName.fromString;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.fest.util.Collections;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.pitest.classinfo.ClassName;
import org.pitest.classpath.CodeSource;
import org.pitest.coverage.CoverageDatabase;
import org.pitest.coverage.TestInfo;
import org.pitest.functional.F;
import org.pitest.functional.FCollection;
import org.pitest.functional.FunctionalList;
import org.pitest.functional.Option;
import org.pitest.mutationtest.build.TestPrioritiser;
import org.pitest.mutationtest.config.PluginServices;
import org.pitest.plugin.ToolClasspathPlugin;

@RunWith(MockitoJUnitRunner.class)
public class AllTestsPrioritiserFactoryTest {

  private AllTestsPrioritiserFactory testee = new AllTestsPrioritiserFactory();

  private ClassName                  foo    = fromString("foo");
  private ClassName                  bar    = fromString("bar");

  @Mock
  private Properties                 props;

  @Mock
  private CodeSource                 code;

  @Mock
  private CoverageDatabase           coverage;

  @Test
  public void shouldReturnTheWholeSuiteForAnyClass() {

    when(code.getCodeUnderTestNames()).thenReturn(Collections.set(foo, bar));
    when(coverage.getTestsForClass(foo)).thenReturn(
        Arrays.asList(test("a"), test("b")));
    when(coverage.getTestsForClass(bar)).thenReturn(Arrays.asList(test("c")));

    TestPrioritiser prioritiser = testee.makeTestPrioritiser(props, code, coverage);
    List<TestInfo> actual = prioritiser.assignTests(null);

    assertThat(actual).containsExactly(test("a"), test("b"), test("c"));

  }

  @Test
  public void shouldNotDupicateTests() {

    when(code.getCodeUnderTestNames()).thenReturn(Collections.set(foo, bar));
    when(coverage.getTestsForClass(foo)).thenReturn(Arrays.asList(test("a")));
    when(coverage.getTestsForClass(bar)).thenReturn(Arrays.asList(test("a")));

    TestPrioritiser prioritiser = testee.makeTestPrioritiser(props, code, coverage);
    List<TestInfo> actual = prioritiser.assignTests(null);

    assertThat(actual).containsExactly(test("a"));

  }

  @Test
  public void shouldProvideDeterminsticOrderForTests() {

    when(code.getCodeUnderTestNames()).thenReturn(Collections.set(foo, bar));
    when(coverage.getTestsForClass(foo)).thenReturn(
        Arrays.asList(test("z"), test("b"), test("e")));
    when(coverage.getTestsForClass(bar)).thenReturn(Arrays.asList(test("a")));

    TestPrioritiser prioritiser = testee.makeTestPrioritiser(props, code, coverage);
    List<TestInfo> actual = prioritiser.assignTests(null);

    assertThat(actual).containsExactly(test("a"), test("b"), test("e"),
        test("z"));

  }

  @Test
  public void shouldDeclareServiceInterface() {
    Iterable<? extends ToolClasspathPlugin> plugins = PluginServices.makeForContextLoader().findToolClasspathPlugins();
    FunctionalList<Class<?>> pluginClasses = FCollection.map(plugins, toClass());
    assertThat(pluginClasses).contains(AllTestsPrioritiserFactory.class);
  }
  
  private static F<ToolClasspathPlugin, Class<?>> toClass() {
    return new  F<ToolClasspathPlugin, Class<?>>() {
      public Class<?> apply(ToolClasspathPlugin a) {
        return a.getClass();
      }
      
    };
  }
  
  private TestInfo test(String name) {
    return new TestInfo("footest", name, 0, Option.<ClassName> none(), 0);
  }

}
