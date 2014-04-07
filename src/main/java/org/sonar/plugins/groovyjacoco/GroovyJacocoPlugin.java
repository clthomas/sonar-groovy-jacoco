/*
 * Sonar Groovy Jacoco
 * Copyright (C) 2010 SonarSource
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.plugins.groovyjacoco;

import java.util.List;

import com.google.common.collect.ImmutableList;

import org.sonar.api.Properties;
import org.sonar.api.Property;
import org.sonar.api.PropertyType;
import org.sonar.api.SonarPlugin;
import org.sonar.plugins.groovyjacoco.foundation.Groovy;
import org.sonar.plugins.groovyjacoco.jacoco.JaCoCoAgentDownloader;
import org.sonar.plugins.groovyjacoco.jacoco.JaCoCoItSensor;
import org.sonar.plugins.groovyjacoco.jacoco.JaCoCoMavenPluginHandler;
import org.sonar.plugins.groovyjacoco.jacoco.JaCoCoOverallSensor;
import org.sonar.plugins.groovyjacoco.jacoco.JaCoCoSensor;
import org.sonar.plugins.groovyjacoco.jacoco.JacocoAntInitializer;
import org.sonar.plugins.groovyjacoco.jacoco.JacocoConfiguration;
import org.sonar.plugins.groovyjacoco.jacoco.JacocoMavenInitializer;

@Properties({
    @Property(
    key = GroovyJacocoPlugin.JACOCO_REPORT_PATH,
    name = "Jacoco Report file",
    description = "Path (absolute or relative) to Jacoco EXEC report in case generation is not handle by the plugin.",
    module = true,
    project = true,
    global = true,
	defaultValue = "target/jacoco.exec"
  ),
  @Property(
    key = GroovyJacocoPlugin.IGNORE_HEADER_COMMENTS,
    defaultValue = "true",
    name = "Ignore Header Comments",
    description =
    "If set to \"true\", the file headers (that are usually the same on each file: licensing information for example) are not considered as comments. " +
      "Thus metrics such as \"Comment lines\" do not get incremented. " +
      "If set to \"false\", those file headers are considered as comments and metrics such as \"Comment lines\" get incremented.",
    project = true, global = true,
    type = PropertyType.BOOLEAN)
})
public class GroovyJacocoPlugin extends SonarPlugin {

  public static final String JACOCO_REPORT_PATH = "sonar.groovy.jacoco.reportPath";
  public static final String IGNORE_HEADER_COMMENTS = "sonar.groovy.ignoreHeaderComments";

  public List getExtensions() {
    return ImmutableList.of(
      //GroovyCommonRulesDecorator.class,
      //GroovyCommonRulesEngine.class,
      // CodeNarc
      //CodeNarcRuleRepository.class,
      //CodeNarcSensor.class,
      //SonarWayProfile.class,
      // Foundation
      Groovy.class,
      //GroovyColorizerFormat.class,
      //GroovySourceImporter.class,
      //GroovyCpdMapping.class,
      // Main sensor
      //GroovySensor.class,
	  //CoberturaMavenPluginHandler.class,

	  // jacoco
	  JacocoConfiguration.class,
	  JaCoCoAgentDownloader.class,
	  JacocoAntInitializer.class,
	  JacocoMavenInitializer.class,
	  JaCoCoMavenPluginHandler.class,
	  JaCoCoSensor.class,
	  JaCoCoItSensor.class,
	  JaCoCoOverallSensor.class

      // Cobertura
	  // CoberturaSensor.class
	);
  }

}
