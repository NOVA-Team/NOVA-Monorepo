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

package nova.core.wrapper.mc.forge.v1_11_2.launcher;

import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickBlock;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import nova.core.item.Item;
import nova.core.item.ItemDictionary;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.VectorConverter;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.block.world.WorldConverter;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.entity.EntityConverter;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.item.ItemConverter;
import nova.internal.core.Game;

/**
 * @author Stan, Calclavia
 */
public class ForgeEventHandler {

	@SubscribeEvent
	public void worldUnload(WorldEvent.Load evt) {
		Game.events().publish(new nova.core.event.WorldEvent.Load(WorldConverter.instance().toNova(evt.getWorld())));
	}

	@SubscribeEvent
	public void worldLoad(WorldEvent.Unload evt) {
		Game.events().publish(new nova.core.event.WorldEvent.Unload(WorldConverter.instance().toNova(evt.getWorld())));
	}

	@SubscribeEvent
	public void onOreRegister(OreDictionary.OreRegisterEvent event) {
		ItemDictionary novaItemDictionary = Game.itemDictionary();

		Item item = ItemConverter.instance().getNovaItem(event.getOre());
		if (!novaItemDictionary.get(event.getName()).contains(item)) {
			novaItemDictionary.add(event.getName(), item);
		}
	}

	@SubscribeEvent
	public void playerInteractEvent(PlayerInteractEvent event) {
		if (event.getWorld() != null) {
			nova.core.event.PlayerEvent.Interact evt = new nova.core.event.PlayerEvent.Interact(
				WorldConverter.instance().toNova(event.getWorld()),
				VectorConverter.instance().toNova(event.getPos()),
				EntityConverter.instance().toNova(event.getEntityPlayer()),
				nova.core.event.PlayerEvent.Interact.Action.values()[toNovaInteractOrdinal(event)]
			);

			Game.events().publish(evt);

			if (event instanceof RightClickBlock) {
				((RightClickBlock)event).setUseBlock(Event.Result.values()[evt.useBlock.ordinal()]);
				((RightClickBlock)event).setUseItem(Event.Result.values()[evt.useItem.ordinal()]);
			} else if (event instanceof LeftClickBlock) {
				((LeftClickBlock)event).setUseBlock(Event.Result.values()[evt.useBlock.ordinal()]);
				((LeftClickBlock)event).setUseItem(Event.Result.values()[evt.useItem.ordinal()]);
			}
			if (event.isCancelable()) event.setCanceled(evt.isCanceled());
		}
	}

	private static int toNovaInteractOrdinal(PlayerInteractEvent event) {
		if (event instanceof RightClickBlock) return 1;
		else if (event instanceof LeftClickBlock) return 2;
		else return 0;
	}
}
