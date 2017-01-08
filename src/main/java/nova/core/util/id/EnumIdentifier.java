/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nova.core.util.id;

import nova.core.retention.Data;

import java.util.Objects;

/**
 *
 * @author ExE Boss
 */
public class EnumIdentifier<E extends Enum<E>> extends AbstractIdentifier<E> {

	public EnumIdentifier(E id) {
		super(id);
	}

	public E asEnum() {
		return id;
	}

	@Override
	public boolean equals(Object other) {
		return equalsImpl(this, other, EnumIdentifier.class, EnumIdentifier::asEnum);
	}

	public static class Loader extends IdentifierLoader<EnumIdentifier<?>> {

		public Loader(String id) {
			super(id);
		}
		
		@Override
		public Class<EnumIdentifier<?>> getIdentifierClass() {
			return cast(EnumIdentifier.class);
		}

		@Override
		public void save(Data data, EnumIdentifier<?> identifier) {
			data.put("id", identifier.asEnum());
		}

		@Override
		public EnumIdentifier<?> load(Data data) {
			return new EnumIdentifier(data.getEnum("id"));
		}

		@SuppressWarnings("unchecked")
		private <T> T cast(Object o) {
			return (T) o;
		}
	}
}
