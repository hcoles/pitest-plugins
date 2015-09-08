package org.pitest.plugins.configreporter;

import org.pitest.mutationtest.ClassMutationResults;
import org.pitest.mutationtest.MutationResultListener;
import org.pitest.util.Log;

import java.util.Properties;
import java.util.logging.Logger;

/**
 * Display user plugin configuration at the beginning of mutation analysis.
 */
public class PluginConfigurationReporter implements MutationResultListener {

  private final static Logger LOG = Log.getLogger();

  private final Properties pluginConfiguration;

  public PluginConfigurationReporter(Properties pluginConfiguration) {
    this.pluginConfiguration = pluginConfiguration;
  }

  @Override
  public void runStart() {
    LOG.info("Mutation analysis was performed with the following plugin configuration: " + prepareConfigurationReport());
  }

  //Visible for testing
  String prepareConfigurationReport() {
    return pluginConfiguration.toString();
  }

  @Override
  public void handleMutationResult(ClassMutationResults results) {
  }

  @Override
  public void runEnd() {
  }
}
