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

package nova.core.wrapper.mc.forge.v1_11_2.wrapper.render.forward;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import nova.core.block.Block;
import nova.core.component.renderer.Renderer;
import nova.core.component.renderer.StaticRenderer;
import nova.core.item.Item;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.VectorConverter;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.block.forward.FWBlock;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.item.ItemConverter;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.render.backward.BWModel;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;
import java.util.Optional;

/**
 * Generates a smart model based on a NOVA Model
 * @author Calclavia
 */
public class FWSmartBlockModel extends FWSmartModel implements IBakedModel {

	private final Block block;
	private final Optional<Item> item;

	public FWSmartBlockModel(Block block) {
		this(block, Optional.empty());
	}

	public FWSmartBlockModel(Block block, Item item) {
		this(block, Optional.of(item));
	}

	@SuppressWarnings("deprecation")
	public FWSmartBlockModel(Block block, Optional<Item> item) {
		this.block = block;
		this.item = item;
		// Change the default transforms to the default full Block transforms
		this.itemCameraTransforms = new ItemCameraTransforms(
			new net.minecraft.client.renderer.block.model.ItemTransformVec3f(new Vector3f(75, 225, 0), new Vector3f(0, 0.1875f, 0.03125f), new Vector3f(0.375f, 0.375f, 0.375f)), // Third Person (Left)
			new net.minecraft.client.renderer.block.model.ItemTransformVec3f(new Vector3f(75, 45, 0), new Vector3f(0, 0.1875f, 0.03125f), new Vector3f(0.375f, 0.375f, 0.375f)), // Third Person (Right)
			new net.minecraft.client.renderer.block.model.ItemTransformVec3f(new Vector3f(0, 225, 0), new Vector3f(0, 0, 0), new Vector3f(0.4f, 0.4f, 0.4f)), // First Person (Left)
			new net.minecraft.client.renderer.block.model.ItemTransformVec3f(new Vector3f(0, 45, 0), new Vector3f(0, 0, 0), new Vector3f(0.4f, 0.4f, 0.4f)), // First Person (Right)
			net.minecraft.client.renderer.block.model.ItemTransformVec3f.DEFAULT, // Head
			new net.minecraft.client.renderer.block.model.ItemTransformVec3f(new Vector3f(30, 225, 0), new Vector3f(0, 0, 0), new Vector3f(0.625f, 0.625f, 0.625f)), // GUI
			net.minecraft.client.renderer.block.model.ItemTransformVec3f.DEFAULT, // Ground
			net.minecraft.client.renderer.block.model.ItemTransformVec3f.DEFAULT);// Fixed
	}

	//Item rendering
	@Override
	public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {
		Item item = ItemConverter.instance().toNova(stack);

		if (item.components.has(Renderer.class) || block.components.has(Renderer.class)) {
			return new FWSmartBlockModel(block, item);
		}

		return FWEmptyModel.INSTANCE;
	}

	@Override
	@SuppressWarnings("deprecation")
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		final Block block;
		if (state != null) {
			FWBlock fwBlock = (FWBlock) state.getBlock();
			block = fwBlock.getBlockInstance(fwBlock.lastExtendedWorld, VectorConverter.instance().toNova(fwBlock.lastExtendedStatePos));
		} else {
			block = this.block;
		}

		BWModel model = new BWModel();
		model.matrix.translate(0.5, 0.5, 0.5);

		if (item.isPresent()) {
			if (item.get().components.has(Renderer.class)) {
				item.get().components.getSet(Renderer.class).forEach(r -> r.onRender.accept(model));
			} else {
				block.components.getSet(Renderer.class).forEach(r -> r.onRender.accept(model));
			}
		} else {
			block.components.getOp(StaticRenderer.class).ifPresent(r -> r.onRender.accept(model));
		}

		return modelToQuads(model, item.map(Item::colorMultiplier));
	}
}
