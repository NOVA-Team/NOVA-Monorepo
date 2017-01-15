/*
 * Copyright (c) 2015 NOVA, All rights reserved.
 * This library is free software, licensed under GNU Lesser General Public License version 3
 *
 * This file is part of NOVA.
 *
 * NOVA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NOVA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NOVA.  If not, see <http://www.gnu.org/licenses/>.
 */

package nova.core.entity;

import nova.core.block.Stateful;
import nova.core.component.ComponentProvider;
import nova.core.component.misc.FactoryProvider;
import nova.core.component.transform.EntityTransform;
import nova.core.util.id.Identifiable;
import nova.core.util.id.UniqueIdentifiable;
import nova.core.util.id.Identifier;
import nova.core.util.id.UUIDIdentifier;
import nova.core.world.World;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * An entity is an object in the world that has a position.
 */
public class Entity extends ComponentProvider implements UniqueIdentifiable, Identifiable, Stateful {

	public final EntityTransform transform() {
		return components.get(EntityTransform.class);
	}

	public final World world() {
		return transform().world();
	}

	public final Vector3D position() {
		return transform().position();
	}

	public final Vector3D scale() {
		return transform().scale();
	}

	public final Vector3D pivot() {
		return transform().pivot();
	}

	public final Rotation rotation() {
		return transform().rotation();
	}

	public final double x() {
		return position().getX();
	}

	public final double y() {
		return position().getY();
	}

	public final double z() {
		return position().getZ();
	}

	public void setWorld(World world) {
		transform().setWorld(world);
	}

	public void setPosition(Vector3D pos) {
		transform().setPosition(pos);
	}

	public void setScale(Vector3D scale) {
		transform().setScale(scale);
	}

	public void setPivot(Vector3D pivot) {
		transform().setPivot(pivot);
	}

	public void setRotation(Rotation rotation) {
		transform().setRotation(rotation);
	}

	public final EntityFactory getFactory() {
		return (EntityFactory) components.get(FactoryProvider.class).factory;
	}

	@Override
	public final Identifier getID() {
		return getFactory().getID();
	}

	@Override
	public UUIDIdentifier getUniqueID() {
		return components.get(UniqueIdentifiable.class).getUniqueID();
	}
}
