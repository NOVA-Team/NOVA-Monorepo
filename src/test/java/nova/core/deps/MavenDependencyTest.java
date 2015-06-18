package nova.core.deps;

import org.junit.Before;
import org.junit.Test;

import static nova.testutils.NovaAssertions.*;

@Dependency(groupId = "test.group.nova",artifactId = "test1", version = "1.0.0")
public class MavenDependencyTest {

	MavenDependency dep;
	@Before
	public void setUp() {
		dep = new MavenDependency(this.getClass().getAnnotation(Dependency.class));
	}

	@Test
	public void testGetDir() throws Exception {
		assertThat(dep.getDir()).isEqualTo("test/group/nova/test1/1.0.0");
	}

	@Test
	public void testGetDownloadURL() throws Exception {
		assertThat(dep.getDownloadURL()).isNotNull();
	}
}