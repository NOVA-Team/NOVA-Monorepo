/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nova.core.util.id;

import nova.core.retention.Data;
import nova.core.util.registry.Registry;

import java.util.Optional;

/**
 * Used to handle loading and saving of identifiers.
 * 
 * @author ExE Boss
 */
public final class IdentifierRegistry extends Registry<IdentifierLoader<?>> {
	private static IdentifierRegistry instance;

	private IdentifierRegistry() {
		register(new StringIdentifier.Loader("String"));
		register(new ClassIdentifier.Loader("Class"));
		register(new UUIDIdentifier.Loader("UUID"));
		register(new EnumIdentifier.Loader("Enum"));
	}

	/**
	 * Returns the IdentifierRegistry instance.
	 * 
	 * @return The IdentifierRegistry instance.
	 */
	public static IdentifierRegistry instance() {
		if (instance == null)
			instance = new IdentifierRegistry();

		return instance;
	}

	/**
	 * Loads an Identifier.
	 * 
	 * @param data The data to load from.
	 * @return The Identifier instance.
	 */
	public Identifier load(Data data) {
		assert data != null;
		StringIdentifier id = new StringIdentifier(data.get("type"));
		if (!contains(id))
			throw new NoSuchIDException("No Identifier class found for type: " + id);
		return get(id).get().load(data);
	}

	/**
	 * Saves an Identifier.
	 * 
	 * @param data The data to save to.
	 * @param identifier The Identifier instance.
	 */
	public void save(Data data, Identifier identifier) {
		final Class<? extends Identifier> clazz = identifier.getClass();
		IdentifierLoader<?> loader = stream().filter(l -> l.getIdentifierClass().equals(clazz)).findFirst()
				.orElse(stream().filter(l -> l.getIdentifierClass().isAssignableFrom(clazz)).findFirst()
				.orElseThrow(() -> new NoSuchIDException("No IdentifierLoader found for class: " + clazz.getSimpleName())));
		loader.save(data, cast(identifier));
		data.put("type", loader.getID().asString());
	}

	@SuppressWarnings("unchecked")
	private <T> T cast(Object o) {
		return (T) o;
	}

	/**
	 * Registers an IdentifierLoader for the given ID.
	 * 
	 * @param loader The loader which will handle saving and loading of the identifier.
	 */
	@Override
	public IdentifierLoader<?> register(IdentifierLoader<?> loader) {
		return super.register(loader);
	}
}
