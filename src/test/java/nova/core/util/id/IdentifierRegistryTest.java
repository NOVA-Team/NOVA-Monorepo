/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nova.core.util.id;

import nova.core.retention.Data;
import nova.core.retention.StorableTest;
import nova.core.util.EnumExample;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;
import java.util.function.Consumer;

import static nova.testutils.NovaAssertions.assertThat;

/**
 * @author ExE Boss
 */
public class IdentifierRegistryTest {
	
	Data data;

	@Before
	public void setUp() {
		data = new Data();
	}

	@Test
	public void testInstance() {
		assertThat(IdentifierRegistry.instance()).isExactlyInstanceOf(IdentifierRegistry.class);
	}

	@Test
	public void testSaveAndLoad() {
		data.put("StringTest", new StringIdentifier("not_namespaced"));
		data.put("NamespacedStringTest", new NamespacedStringIdentifier("nova:namespaced"));
		data.put("ClassTest", new ClassIdentifier(ClassIdentifier.class));
		data.put("UUIDTest", new UUIDIdentifier(new UUID(0, 0)));
		data.put("EnumTest", new EnumIdentifier<>(EnumExample.EXAMPLE_42));
		
		StorableTest.printData(data, System.out::println);
		System.out.println();
		printIdentifier(data.getIdentifier("StringTest"), System.out::println);
		printIdentifier(data.getIdentifier("NamespacedStringTest"), System.out::println);
		printIdentifier(data.getIdentifier("ClassTest"), System.out::println);
		printIdentifier(data.getIdentifier("UUIDTest"), System.out::println);
		printIdentifier(data.getIdentifier("EnumTest"), System.out::println);
	}
	
	private static void printIdentifier(Identifier identifier, Consumer<String> print) {
		print.accept(identifier.getClass() + ": " + identifier.asString());
	}
}
