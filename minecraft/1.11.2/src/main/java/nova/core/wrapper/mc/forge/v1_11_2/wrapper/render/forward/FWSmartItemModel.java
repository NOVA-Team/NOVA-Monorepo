
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
import nova.core.component.renderer.Renderer;
import nova.core.item.Item;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.item.ItemConverter;
import nova.core.wrapper.mc.forge.v1_11_2.wrapper.render.backward.BWModel;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;

/**
 * Generates a smart model based on a NOVA Model
 * @author Calclavia
 */
@SuppressWarnings("deprecation")
public class FWSmartItemModel extends FWSmartModel implements IBakedModel {

	private final Item item;

	@SuppressWarnings("deprecation")
	public FWSmartItemModel(Item item) {
		this.item = item;
		// Change the default transforms to the default Item transforms
		this.itemCameraTransforms = new ItemCameraTransforms(
			new net.minecraft.client.renderer.block.model.ItemTransformVec3f(new Vector3f(0, -90, -130), new Vector3f(0, 1f / 24f, -2.75f / 16f), new Vector3f(0.9f, 0.9f, 0.9f)), // Third Person (Left)
			new net.minecraft.client.renderer.block.model.ItemTransformVec3f(new Vector3f(0, -90, -130), new Vector3f(0, 1f / 24f, -2.75f / 16f), new Vector3f(0.9f, 0.9f, 0.9f)), // Third Person (Right)
			new net.minecraft.client.renderer.block.model.ItemTransformVec3f(new Vector3f(0, -135, 25), new Vector3f(0, 0.25f, 0.125f), new Vector3f(1.7f, 1.7f, 1.7f)), // First Person (Left)
			new net.minecraft.client.renderer.block.model.ItemTransformVec3f(new Vector3f(0, -135, 25), new Vector3f(0, 0.25f, 0.125f), new Vector3f(1.7f, 1.7f, 1.7f)), // First Person (Rigth)
			net.minecraft.client.renderer.block.model.ItemTransformVec3f.DEFAULT, // Head
			net.minecraft.client.renderer.block.model.ItemTransformVec3f.DEFAULT, // new ItemTransformVec3f(new Vector3f(-30, 135, 0), new Vector3f(), new Vector3f(1.6F, 1.6F, 1.6F)), // GUI
			net.minecraft.client.renderer.block.model.ItemTransformVec3f.DEFAULT, // Ground
			net.minecraft.client.renderer.block.model.ItemTransformVec3f.DEFAULT);// Fixed
	}

	//Item rendering
	@Override
	public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {
		Item item = ItemConverter.instance().toNova(stack);

		if (item.components.has(Renderer.class)) {
			return new FWSmartItemModel(item);
		}

		return FWEmptyModel.INSTANCE;
	}

	@Override
	@SuppressWarnings("deprecation")
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		BWModel model = new BWModel();
		model.matrix.translate(0.5, 0.5, 0.5);
		item.components.getSet(Renderer.class).forEach(r -> r.onRender.accept(model));
		return modelToQuads(model, item.colorMultiplier());
	}

	@Override
	public boolean isGui3d() {
		return item.components.has(Renderer.class);
	}
}
