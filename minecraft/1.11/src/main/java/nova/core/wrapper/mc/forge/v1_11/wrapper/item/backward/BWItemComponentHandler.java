/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nova.core.wrapper.mc.forge.v1_11.wrapper.item.backward;

/**
 * Adds additional components to the NOVA item.
 *
 * @author ExE Boss
 */
public interface BWItemComponentHandler {

	void addComponents(BWItem novaItem, net.minecraft.item.Item mcItem);

	static void register(BWItemComponentHandler handler) {
		BWItemFactory.registerComponentHandler(handler);
	}
}
