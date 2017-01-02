/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nova.core.wrapper.mc.forge.v1_11.wrapper.block.backward;

/**
 * Adds additional components to the NOVA block.
 *
 * @author ExE Boss
 */
public interface BWBlockComponentHandler {

	void addComponents(BWBlock novaBlock, net.minecraft.block.Block mcBlock);

	static void register(BWBlockComponentHandler handler) {
		BWBlock.registerComponentHandler(handler);
	}
}
