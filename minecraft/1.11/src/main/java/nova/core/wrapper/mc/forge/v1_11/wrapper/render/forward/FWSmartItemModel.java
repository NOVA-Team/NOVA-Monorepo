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

package nova.core.wrapper.mc.forge.v1_11.wrapper.render.forward;

import nova.core.wrapper.mc.forge.v1_11.wrapper.render.backward.BWModel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import nova.core.component.renderer.ItemRenderer;
import nova.core.item.Item;
import nova.core.wrapper.mc.forge.v1_11.render.RenderUtility;
import nova.internal.core.Game;
import org.lwjgl.util.vector.Vector3f;

import java.util.Collections;
import java.util.List;

/**
 * Generates a smart model based on a NOVA Model
 * @author Calclavia
 */
public class FWSmartItemModel extends FWSmartModel implements IBakedModel {

	private final Item item;

	public FWSmartItemModel(Item item) {
		super();
		this.item = item;
		// Change the default transforms to the default Item transforms
		this.itemCameraTransforms = new ItemCameraTransforms(
			new ItemTransformVec3f(new Vector3f(-90, 0, 0), new Vector3f(0, 1, -3), new Vector3f(0.55f, 0.55f, 0.55f)), // Third Person
			new ItemTransformVec3f(new Vector3f(-90, 0, 0), new Vector3f(0, 1, -3), new Vector3f(0.55f, 0.55f, 0.55f)), // Third Person
			new ItemTransformVec3f(new Vector3f(0, -135, 25), new Vector3f(0, 4, 2), new Vector3f(1.7f, 1.7f, 1.7f)), // First Person
			new ItemTransformVec3f(new Vector3f(0, -135, 25), new Vector3f(0, 4, 2), new Vector3f(1.7f, 1.7f, 1.7f)), // First Person
			ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT,
			ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT);
	}

	public IBakedModel handleItemState(ItemStack stack) {
		Item item = Game.natives().toNova(stack);

		if (item.components.has(ItemRenderer.class)) {
			return new FWSmartItemModel(item);
		}

		return this;
	}

	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		if (item.components.has(ItemRenderer.class)) {
			BWModel model = new BWModel();
			ItemRenderer renderer = item.components.get(ItemRenderer.class);
			model.matrix.translate(0.5, 0.5, 0.5);
			renderer.onRender.accept(model);
			return modelToQuads(model);
		}

		return Collections.emptyList();
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		if (item.components.has(ItemRenderer.class)) {
			ItemRenderer itemRenderer = item.components.get(ItemRenderer.class);
			if (itemRenderer.texture.isPresent()) {
				return RenderUtility.instance.getTexture(itemRenderer.texture.get());
			}
		}

		return null;
	}

	@Override
	public boolean isGui3d() {
		return item.components.has(ItemRenderer.class);
	}

	@Override
	public ItemOverrideList getOverrides() {
		return ItemOverrideList.NONE;
	}
}
