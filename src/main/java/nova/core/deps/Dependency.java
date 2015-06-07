package nova.core.deps;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(Dependencies.class)
public @interface Dependency {

	String mavenRepo() default "http://maven.novaapi.net/";
	String groupId();
	String artifactId();
	String version();
	String classifier() default "";
	String ext() default "jar";

}
