/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nova.core.wrapper.mc.forge.v17.wrapper.block.backward;

import nova.core.block.BlockFactory;

/**
 *
 * @author ExE Boss
 */
public class BWBlockFactory extends BlockFactory {
	private final net.minecraft.block.Block block;

	public BWBlockFactory(net.minecraft.block.Block block) {
		super(net.minecraft.block.Block.blockRegistry.getNameForObject(block).toString(), () -> new BWBlock(block), evt -> {});

		this.block = block;
	}

	public net.minecraft.block.Block getBlock() {
		return block;
	}
}
