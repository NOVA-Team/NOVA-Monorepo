package nova.core.deps;

import java.lang.annotation.*;

/**
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
