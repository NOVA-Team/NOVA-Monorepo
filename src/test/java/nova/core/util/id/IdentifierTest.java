package nova.core.util.id;

import nova.testutils.NovaAssertions;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static nova.testutils.NovaAssertions.assertThat;

/**
 * @author soniex2
 */
public class IdentifierTest {

	@Test
	public void testStringIdentifierComparison() {
		StringIdentifier a1 = new StringIdentifier("a");
		StringIdentifier a2 = new StringIdentifier("a");
		StringIdentifier b = new StringIdentifier("b");
		assertThat(a1).isEqualTo(a2);
		assertThat(a1).isNotEqualTo(b);
		assertThat(a1.hashCode()).isEqualTo(a2.hashCode());
		assertThat(a1.asString()).isEqualTo(a2.asString());
		assertThat(a1.asString()).isNotEqualTo(b.asString());
	}

	@Test
	public void testMixedIdentifierMap() {
		Map<Identifier, String> map = new HashMap<>();

		map.put(new StringIdentifier(StringIdentifier.class.getName()), "StringIdentifier class name");
		map.put(new ClassIdentifier(StringIdentifier.class), "StringIdentifier class");

		assertThat(map.get(new StringIdentifier(StringIdentifier.class.getName()))).isEqualTo("StringIdentifier class name");
		assertThat(map.get(new StringIdentifier(StringIdentifier.class.getName()))).isNotEqualTo(map.get(new ClassIdentifier(StringIdentifier.class)));
		assertThat(map.get(new ClassIdentifier(StringIdentifier.class))).isEqualTo("StringIdentifier class");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidNamespace() {
		NamespacedStringIdentifier nsi = new NamespacedStringIdentifier("nova:nova", "nova");
	}

	@Test
	public void testStringVsNamespace() {
		NamespacedStringIdentifier nsi = new NamespacedStringIdentifier("", "a");
		StringIdentifier si = new StringIdentifier("a");
		assertThat(nsi).isNotEqualTo(si);
	}

	@Test
	public void testUUID() {
		UUIDIdentifier a = new UUIDIdentifier(UUID.fromString("27b8a073-a6ed-4455-b144-92000c7394b7"));
		UUIDIdentifier b = new UUIDIdentifier(UUID.fromString("27b8a073-a6ed-4455-b144-92000c7394b7"));
		UUIDIdentifier c = new UUIDIdentifier(UUID.randomUUID());

		assertThat(a).isEqualTo(b);
		assertThat(a.hashCode()).isEqualTo(b.hashCode());
		assertThat(a.asUUID()).isEqualTo(UUID.fromString("27b8a073-a6ed-4455-b144-92000c7394b7"));
		assertThat(a).isNotEqualTo(c); // this has a 1 in 2^122 chance of failing. it's *fine*.
	}
}
