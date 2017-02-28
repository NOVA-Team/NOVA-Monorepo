package nova.core.component;

import nova.core.component.exception.ComponentException;
import org.junit.Before;
import org.junit.Test;

import static nova.testutils.NovaAssertions.assertThat;

/**
 * Tests the component provider's addition and extraction of components
 * @author Calclavia
 */
public class ComponentProviderTest {

	private ComponentProvider<?> provider;

	@Before
	public void setUp() throws Exception {
		// TODO: When Java 9 comes out, use the diamond operator
		provider = new ComponentProvider<ComponentMap>() {};
	}

	@Test
	public void testComponentAdd() throws Exception {
		provider.components.add(new Category("Test"));
		assertThat(provider.components.has(Category.class)).isTrue();
		assertThat(provider.components.getOp(Category.class)).isPresent();
	}

	@Test(expected = ComponentException.class)
	public void testComponentDuplicateAdd() throws Exception {
		provider.components.add(new Category("Test"));
		provider.components.add(new Category("Test"));
	}

	@Test(expected = ComponentException.class)
	public void testComponentNullGet() throws Exception {
		assertThat(provider.components.get(Category.class));
	}

	@Test
	public void testComponentGetOrAdd() throws Exception {
		provider.components.getOrAdd(new Category("Test"));
		assertThat(provider.components.getOp(Category.class)).isPresent();
		provider.components.getOrAdd(new Category("Test"));
		assertThat(provider.components.getOp(Category.class)).isPresent();
		assertThat(provider.components).hasSize(1);
	}

}
