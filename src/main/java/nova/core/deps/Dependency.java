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



import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;/**
 * This annotation describes dependency.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(Dependencies.class)
public @interface Dependency {

	/**
	 * Maven URL to seach for this depenency.
	 * @return URL parsable string.
	 */
	String mavenRepo() default "http://maven.novaapi.net/";

	/**
	 * Group to look for.
	 * @return group path.
	 */
	String groupId();

	/**
	 * Artifact to look for.
	 * @return artifact name.
	 */
	String artifactId();

	/**
	 * Required version using.
	 * @return version ot the artifact.
	 */
	String version();

	/**
	 * Calssifier to use.
	 * @return classifier of artifact to look for.
	 */
	String classifier() default "";

	/**
	 * Extension of the artifact.
	 * Defaults to {@code jar}
	 * @return extension to look for.
	 */
	String ext() default "jar";

}
