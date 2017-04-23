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

package nova.core.wrapper.mc.forge.v17.wrapper.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import nova.core.component.renderer.Renderer;
import nova.core.item.Item;
import nova.core.item.ItemFactory;
import nova.core.util.Direction;
import nova.core.wrapper.mc.forge.v17.wrapper.entity.backward.BWEntity;
import nova.core.wrapper.mc.forge.v17.wrapper.render.backward.BWModel;
import nova.internal.core.Game;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.List;
import java.util.Optional;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_BIT;

/**
 * An interface implemented by ItemBlockWrapper and ItemWrapper classes to override Minecraft's item events.
 * @author Calclavia
 */
public interface ItemWrapperMethods extends IItemRenderer {

	ItemFactory getItemFactory();

	@SuppressWarnings({"unchecked", "rawtypes"})
	default void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean p_77624_4_) {
		Item item = Game.natives().toNova(itemStack);
		item.setCount(itemStack.stackSize).events.publish(new Item.TooltipEvent(Optional.of(new BWEntity(player)), list));
		getItemFactory().save(item);
	}

	default boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		Item item = Game.natives().toNova(itemStack);
		Item.UseEvent event = new Item.UseEvent(new BWEntity(player), new Vector3D(x, y, z), Direction.fromOrdinal(side), new Vector3D(hitX, hitY, hitZ));
		item.events.publish(event);
		ItemConverter.instance().updateMCItemStack(itemStack, item);
		return event.action;
	}

	default ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		Item item = Game.natives().toNova(itemStack);
		item.events.publish(new Item.RightClickEvent(new BWEntity(player)));
		return ItemConverter.instance().updateMCItemStack(itemStack, item);
	}

	default IIcon getIconFromDamage(int p_77617_1_) {
		return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("missingno");
	}

	default IIcon getIcon(ItemStack itemStack, int pass) {
		return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("missingno");
	}

	@Override
	default boolean handleRenderType(ItemStack item, IItemRenderer.ItemRenderType type) {
		return item.getItem() == this;
	}

	@Override
	default boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType type, ItemStack item, IItemRenderer.ItemRendererHelper helper) {
		return true;
	}

	@Override
	default void renderItem(IItemRenderer.ItemRenderType type, ItemStack itemStack, Object... data) {
		Item item = Game.natives().toNova(itemStack);
		if (item.components.has(Renderer.class)) {
			GL11.glPushAttrib(GL_TEXTURE_BIT);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glPushMatrix();
			Tessellator.instance.startDrawingQuads();
			BWModel model = new BWModel();
			switch (type) {
				case EQUIPPED:
					break;
				case EQUIPPED_FIRST_PERSON:
					break;
				case INVENTORY:
					model.matrix.rotate(Direction.DOWN.toVector(), Math.PI / 4);
					model.matrix.rotate(Direction.EAST.toVector(), Math.PI / 6);
					model.matrix.scale(1.6, 1.6, 1.6);
					break;
			}
			item.components.getSet(Renderer.class).forEach(r -> r.onRender.accept(model));
			model.render();
			Tessellator.instance.draw();
			GL11.glPopMatrix();
			GL11.glPopAttrib();
		}
	}

	default int getColorFromItemStack(ItemStack itemStack, int p_82790_2_) {
		return ((Item) Game.natives().toNova(itemStack)).colorMultiplier().argb();
	}
}
