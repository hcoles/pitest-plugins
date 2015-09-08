package org.pitest.plugins.configreporter;

import org.junit.Test;

import java.util.Properties;

import static org.fest.assertions.api.Assertions.assertThat;

public class PluginConfigurationReporterTest {

    @Test
    public void shouldFoo() {
        //given
        Properties props = new Properties();
        props.put("testPlugin.prop1", "value1");
        props.put("testPlugin.prop2", "value2");
        //and
        PluginConfigurationReporter reporter = new PluginConfigurationReporter(props);
        //when
        String report = reporter.prepareConfigurationReport();
        //then
        assertThat(report).isEqualTo("{testPlugin.prop2=value2, testPlugin.prop1=value1}");
    }
}