/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nova.core.util.id;

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
}
