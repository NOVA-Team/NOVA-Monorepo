/*
 * Copyright (c) 2017 NOVA, All rights reserved.
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
package nova.core.wrapper.mc.forge.v1_11_2.wrapper.item.forward;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import nova.core.item.Item;
import nova.core.retention.Data;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.data.NBTStorable;

/**
 * Internal NOVA capability used to make NOVA items persistent.
 *
 * @author ExE Boss
 */
public class NovaItem implements NBTStorable {
	@CapabilityInject(NovaItem.class)
	public static Capability<NovaItem> CAPABILITY = null;

	public final Item item;

	public NovaItem(Item item) {
		this.item = item;
	}

	@Override
	public void save(Data data) {
		item.save(data);
	}

	@Override
	public void load(Data data) {
		item.load(data);
	}
}
