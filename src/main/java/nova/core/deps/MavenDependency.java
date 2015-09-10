/*
 * Copyright (c) 2015 NOVA, All rights reserved.
 * This library is free software, licensed under GNU Lesser General Public License version 3
 *
 * This file is part of NOVA.
 *
 * NOVA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NOVA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NOVA.  If not, see <http://www.gnu.org/licenses/>.
 */

package nova.core.deps;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;

/**
 * @author rx14
 */
public class MavenDependency {

	final String repoURL;
	final String groupID;
	final String artifactID;
	final String version;
	final String classifier;
	final String ext;

	public MavenDependency(String mavenRepo,
	                       String groupId,
	                       String artifactId,
	                       String version,
	                       String classifier,
	                       String ext) {
		this.repoURL = mavenRepo.isEmpty() ? "http://maven.novaapi.net/" : mavenRepo;

		this.groupID = groupId;
		this.artifactID = artifactId;
		this.version = version;

		this.classifier = classifier;
		this.ext = ext;
	}

	/**
	 * Creates new maven dependency using Dependency annotation.
	 * @param annotation to be used as a source of data.
	 */
	public MavenDependency(Dependency annotation) {
		this.repoURL = annotation.mavenRepo();

		this.groupID = annotation.groupId();
		this.artifactID = annotation.artifactId();
		this.version = annotation.version();

		this.classifier = annotation.classifier();
		this.ext = annotation.ext();
	}

	public String getDir() {
		return this.groupID.replaceAll("\\.", "/") + "/" + this.artifactID + "/" + this.version;
	}

	public String getPath() {
		return MessageFormat.format("{0}/{1}-{2}{3}.{4}",
		                    /*{0}*/ getDir(),
							/*{1}*/ this.artifactID,
		                    /*{2}*/ this.version,
		                    /*{3}*/ this.classifier.isEmpty() ? "" : "-" + this.classifier,
		                    /*{4}*/ ext);
	}

	public URL getDownloadURL() {
		try {
			return new URL(this.repoURL + getPath());
		} catch (MalformedURLException e) {
			throw new DependencyException(e);
		}
	}
}
