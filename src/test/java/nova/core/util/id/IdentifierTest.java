/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nova.core.util.id;

import nova.core.util.EnumExample;
import org.junit.Test;

import java.util.UUID;

import static nova.testutils.NovaAssertions.assertThat;

/**
 * @author ExE Boss
 */
public class IdentifierTest {

	@Test
	public void stringEquals() {
		StringIdentifier str = new StringIdentifier("not_namespaced");
		StringIdentifier namespaced1 = new StringIdentifier("nova:namespaced");
		NamespacedStringIdentifier namespaced2 = new NamespacedStringIdentifier("nova", "namespaced");

		assertThat(str.asString()).isEqualTo("not_namespaced");
		assertThat(namespaced1).isEqualTo(namespaced2);
	}

	@Test
	public void classEquals() {
		ClassIdentifier clazz = new ClassIdentifier(IdentifierTest.class);

		assertThat(clazz.asClass()).isEqualTo(IdentifierTest.class);
		assertThat(clazz.asString()).isEqualTo(IdentifierTest.class.getSimpleName());
		assertThat(clazz).isEqualTo(new ClassIdentifier(IdentifierTest.class));
	}

	@Test
	public void uuidEquals() {
		UUIDIdentifier uuid = new UUIDIdentifier(new UUID(0, 0));

		assertThat(uuid.asUUID()).isEqualTo(new UUID(0, 0));
		assertThat(uuid.asString()).isEqualTo(new UUID(0, 0).toString());
		assertThat(uuid).isEqualTo(new UUIDIdentifier(new UUID(0, 0)));
	}

	@Test
	public void enumEquals() {
		EnumIdentifier<EnumExample> enumIdentifier = new EnumIdentifier<>(EnumExample.EXAMPLE_42);

		assertThat(enumIdentifier.asEnum()).isEqualTo(EnumExample.EXAMPLE_42);
		assertThat(enumIdentifier.asString()).isEqualTo(EnumExample.EXAMPLE_42.toString());
		assertThat(enumIdentifier).isEqualTo(new EnumIdentifier<>(EnumExample.EXAMPLE_42));
	}
}
