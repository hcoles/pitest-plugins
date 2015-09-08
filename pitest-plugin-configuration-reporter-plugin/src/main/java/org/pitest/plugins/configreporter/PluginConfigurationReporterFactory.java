package org.pitest.plugins.configreporter;

import org.pitest.mutationtest.ListenerArguments;
import org.pitest.mutationtest.MutationResultListener;
import org.pitest.mutationtest.MutationResultListenerFactory;

import java.util.Properties;

public class PluginConfigurationReporterFactory implements MutationResultListenerFactory {

  public String description() {
    return "Plugin configuration reporter";
  }

  @Override
  public MutationResultListener getListener(Properties props, ListenerArguments args) {
    return new PluginConfigurationReporter(props);
  }

  @Override
  public String name() {
    return "pluginConfigurationReporter";
  }
}
