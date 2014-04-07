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
package org.sonar.plugins.groovyjacoco.jacoco;

import java.util.Collection;
import java.util.Iterator;

import org.sonar.api.batch.DependsUpon;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FilePredicate;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.measures.Measure;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;
import org.sonar.api.scan.filesystem.PathResolver;
import org.sonar.plugins.groovyjacoco.foundation.Groovy;

public class JaCoCoSensor implements Sensor {

  private JacocoConfiguration configuration;
  private final ResourcePerspectives perspectives;
  private final FileSystem fileSystem;
  private final PathResolver pathResolver;

  public JaCoCoSensor(JacocoConfiguration configuration, ResourcePerspectives perspectives, FileSystem fileSystem, PathResolver pathResolver) {
    this.configuration = configuration;
    this.perspectives = perspectives;
    this.fileSystem = fileSystem;
    this.pathResolver = pathResolver;
  }

  @DependsUpon
  public String dependsUponSurefireSensors() {
    return "surefire-groovy";
  }

  public void analyse(Project project, SensorContext context) {
    new UnitTestsAnalyzer(perspectives).analyse(project, context);
  }

  public boolean shouldExecuteOnProject(Project project) {
	  if(!configuration.isEnabled(project)) {
		  JaCoCoUtils.LOG.warn("Jacoco is not enabled for project " + project.getName());
		  return false;
	  }

	  final Iterator<InputFile> inputFiles = fileSystem.inputFiles(groovyPredicate()).iterator();
	  boolean runGroovyJacoco = inputFiles.hasNext();
	  if (runGroovyJacoco)
	  {
		  return true;
	  }
	  JaCoCoUtils.LOG.warn("No groovy files found in project " + project.getName() + ", skipping.");
	  return false;
  }

	private FilePredicate groovyPredicate()
	{
		return new FilePredicate() {
			public boolean apply(final InputFile inputFile) {
				//System.out.println(inputFile.absolutePath());
				return inputFile.language().equals(Groovy.KEY);
			}
		};
	}

  class UnitTestsAnalyzer extends AbstractAnalyzer {
    public UnitTestsAnalyzer(ResourcePerspectives perspectives) {
      super(perspectives, fileSystem, pathResolver);
    }

    @Override
    protected String getReportPath(Project project) {
      return configuration.getReportPath();
    }

    @Override
    protected String getExcludes(Project project) {
      return configuration.getExcludes();
    }

	  @Override
	  protected void saveMeasures(SensorContext context, Resource resource, Collection<Measure> measures) {

		  for (Measure measure : measures)
		  {
			  context.saveMeasure(resource, measure);
		  }

	  }
  }



	@Override
  public String toString() {
    return getClass().getSimpleName();
  }

}
