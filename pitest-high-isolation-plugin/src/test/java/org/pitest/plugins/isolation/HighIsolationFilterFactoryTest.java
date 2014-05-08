package org.pitest.plugins.isolation;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Test;
import org.pitest.classpath.CodeSource;
import org.pitest.functional.F;
import org.pitest.functional.FCollection;
import org.pitest.functional.FunctionalList;
import org.pitest.mutationtest.config.PluginServices;
import org.pitest.plugin.ToolClasspathPlugin;
import org.pitest.plugins.isolation.HighIsolationFilterFactory;

public class HighIsolationFilterFactoryTest {

  @Test
  public void shouldCreateHighIsolationFilters() {
    HighIsolationFilterFactory testee = new HighIsolationFilterFactory();
    assertThat(testee.createFilter(someSource(), someInt())).isNotNull();
  }

  @Test
  public void shouldDeclareServiceInterface() {
    // plugins must declare the services they provide with entries in src/main/resources/META-INF/services
    Iterable<? extends ToolClasspathPlugin> plugins = PluginServices.makeForContextLoader().findToolClasspathPlugins();
    FunctionalList<Class<?>> pluginClasses = FCollection.map(plugins, toClass());
    assertThat(pluginClasses).contains(HighIsolationFilterFactory.class);
  }
  
  private static F<ToolClasspathPlugin, Class<?>> toClass() {
    return new  F<ToolClasspathPlugin, Class<?>>() {
      public Class<?> apply(ToolClasspathPlugin a) {
        return a.getClass();
      }
      
    };
  }

  private int someInt() {
    return 0;
  }

  private CodeSource someSource() {
    return null;
  }

}
