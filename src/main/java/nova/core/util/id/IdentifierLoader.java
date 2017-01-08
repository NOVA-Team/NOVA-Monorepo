/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nova.core.util.id;

import nova.core.retention.Data;

/**
 * Class used to load identifiers.
 *
 * @author ExE Boss
 * @param <T> The Identifier class
 * @param <N> The Identifier type
 * @see Identifier
 * @see IdentifierRegistry
 */
public abstract class IdentifierLoader<T extends Identifier> implements Identifiable {

	protected final String id;

	/**
	 * @param id The ID of the Identifier to load. MUST be a string, as we will use this to determine the type.
	 */
	public IdentifierLoader(String id) {
		this.id = id;
	}

	public abstract Class<T> getIdentifierClass();

	public abstract void save(Data data, T identifier);

	public abstract T load(Data data);

	/**
	 * This method creates a new Identifier instance.
	 * 
	 * Implementations must ensure that
	 * {@link Object#equals(java.lang.Object) identifier.equals}{@code (load(}{@link Identifier#asString() identifier.asString()}{@code ))}
	 * is always true.
	 * 
	 * @param data 
	 * @return 
	 */
	public abstract T load(String data);

	@Override
	public final StringIdentifier getID() {
		return new StringIdentifier(id);
	}
}
