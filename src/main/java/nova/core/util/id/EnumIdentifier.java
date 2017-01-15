/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nova.core.util.id;

import nova.core.retention.Data;
import nova.core.retention.DataException;

/**
 *
 * @author ExE Boss
 */
public class EnumIdentifier<E extends Enum<E>> extends AbstractIdentifier<E> {

	public EnumIdentifier(E id) {
		super(id);
	}

	@Override
	public String asString() {
		return id.getClass() + "::" + id.name();
	}

	@Override
	public String asShortString() {
		return id.name();
	}

	public E asEnum() {
		return id;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public final boolean equals(Object other) {
		return equalsImpl(this, other, EnumIdentifier.class, EnumIdentifier::asEnum);
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
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
			data.put("id", identifier.id.getClass().getName());
			data.put("value", identifier.asEnum().name());
		}

		@Override
		public EnumIdentifier<?> load(Data data) {
			try {
				Class<? extends Enum> enumClass = (Class<? extends Enum>) Class.forName(data.get("id"));
				return new EnumIdentifier(Enum.valueOf(enumClass, data.get("value")));
			} catch (Exception e) {
				throw new DataException(e);
			}
		}

		@Override
		public EnumIdentifier<?> load(String data) {
			try {
				Class<? extends Enum> enumClass = (Class<? extends Enum>) Class.forName(data.substring(0, data.lastIndexOf("::")));
				return new EnumIdentifier(Enum.valueOf(enumClass, data.substring(data.lastIndexOf("::") + 2)));
			} catch (Exception e) {
				throw new DataException(e);
			}
		}

		@SuppressWarnings("unchecked")
		private <T> T cast(Object o) {
			return (T) o;
		}
	}
}
