# pitest-plugins

Example plugins for pitest.

This repository contains examples of creating plugins for the pitest mutation testing system. Several of them may be useful in their own right, particularly to those working in academic research.

These plugins are not currently released. To install locally from source

```bash
mvn install
```

To use these plugins via maven add them as dependencies to the pitest-maven plugin (i.e **not** to your project dependencies) e.g

```xml
      <plugin>
        <groupId>org.pitest</groupId>
        <artifactId>pitest-maven</artifactId>
        <version>0.34-SNAPSHOT</version>

        <dependencies>
          <dependency>
            <groupId>org.pitest.plugins</groupId>
            <artifactId>pitest-high-isolation-plugin</artifactId>
            <version>0.1-SNAPSHOT</version>
          </dependency>
        </dependencies>

        <configuration>
		blah
        </configuration>
      </plugin>
```

# The examples

## High isolation plugin

This plugin uses the MutationFilterFactory extension point to increase the isolation between mutants.

By default pitest isolates mutations from different classes into different JVMs so there can be no inteference between them. It is still possible for mutations within the *same* class to interfere with each other if they poison static state in the JVM. Pitest will try to detect and prevent interference in some obvious cases where this might occur (e.g static initializers) but it does not make a guarantee that it will be prevented.

This design is a trade-off between speed and correctness.

This plugin changes that trade off so that pitest pessimitically isolates every mutation using classloaders. When this plugin is used pitest guarantees that mutations cannot interfere with each other via static state at the expense of greatly reduced performance.

To activate this plugin place it on the tool classpath (**not** your project classpath).

You should see it listed in the loaded plugins at startup and see the string

`"Marking all mutations as requiring isolation"`

in the log.

Mutation testing will also take a **lot** longer.

**If you require complete isolation with a seperate JVM for each mutant for your research project this can be achieved without this plugin by setting the parameter mutationUnitSize to 1. This will be very very slow but should guarantee complete isolation.**

## All tests plugin

This plugin uses the TestPrioritiserFactory extension point to dumbly run the entire test suite against each mutation.

By default pitest only runs tests against a mutation that coverage data suggests will actually exercise the mutation.

In rare cases this strategy might result in a potentialy killing test not being run against a mutation e.g if the mutation is within some code exercised when a singleton is initialised. In this case only first test case to use the singleton will be registered as covering this code. If other test cases were run individually (i.e not as part of a suite) then they would also exercise the code and might detect the mutation.

This plugin avoids these corner cases by ignoring coverage data and always running the full suite of tests. To ensure deterministic behaviour the tests are order alphabetically.

To activate this plugin place it on the too classpath - you should see it listed in the loaded plugins at startup.

This plugin will make mutation testing **very** slow.


