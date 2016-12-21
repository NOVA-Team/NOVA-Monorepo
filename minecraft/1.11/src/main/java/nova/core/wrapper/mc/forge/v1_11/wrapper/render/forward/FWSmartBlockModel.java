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
import nova.core.block.Block;
import nova.core.component.renderer.ItemRenderer;
import nova.core.component.renderer.StaticRenderer;
import nova.core.item.ItemBlock;
import nova.core.wrapper.mc.forge.v1_11.render.RenderUtility;
import nova.core.wrapper.mc.forge.v1_11.wrapper.block.forward.FWBlock;
import nova.internal.core.Game;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;

/**
 * Generates a smart model based on a NOVA Model
 * @author Calclavia
 */
public class FWSmartBlockModel extends FWSmartModel implements IBakedModel {

	private final Block block;
	private final boolean isItem;

	public FWSmartBlockModel(Block block, boolean isDummy) {
		super();
		this.block = block;
		this.isItem = isDummy;
		// Change the default transforms to the default full Block transforms
		this.itemCameraTransforms = new ItemCameraTransforms(
				new ItemTransformVec3f(new Vector3f(10, -45, 170), // Third Person (Left)
						new Vector3f(0, 0.09375f, -0.171875f), new Vector3f(0.375f, 0.375f, 0.375f)),
				new ItemTransformVec3f(new Vector3f(10, -45, 170), // Third Person (Right)
						new Vector3f(0, 0.09375f, -0.171875f), new Vector3f(0.375f, 0.375f, 0.375f)),
				ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT, // First Person (Left, Right)
				ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT, // Head, Gui
				ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT); // Ground, Fixed
	}

	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		BWModel blockModel = new BWModel();
		blockModel.matrix.translate(0.5, 0.5, 0.5);

		if (isItem) {
			ItemRenderer renderer = block.components.get(ItemRenderer.class);
			renderer.onRender.accept(blockModel);
		} else {
			StaticRenderer renderer = block.components.get(StaticRenderer.class);
			renderer.onRender.accept(blockModel);
		}

		return modelToQuads(blockModel);
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		/*
		if (block.components.has(StaticRenderer.class)) {
			Optional<Texture> apply = block.components.get(StaticRenderer.class).texture.apply(Direction.UNKNOWN);
			if (apply.isPresent()) {
				return RenderUtility.instance.getTexture(apply.components.get());
			}
		}*/

		if (block.components.has(ItemRenderer.class)) {
			ItemRenderer itemRenderer = block.components.get(ItemRenderer.class);
			if (itemRenderer.texture.isPresent()) {
				return RenderUtility.instance.getTexture(itemRenderer.texture.get());
			}
		}

		return null;
	}

	@Override
	public ItemOverrideList getOverrides() {
		return ItemOverrideList.NONE;
	}
}
