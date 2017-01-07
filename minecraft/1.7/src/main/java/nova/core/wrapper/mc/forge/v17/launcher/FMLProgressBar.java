/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nova.core.wrapper.mc.forge.v17.launcher;

import cpw.mods.fml.common.ProgressManager.ProgressBar;

/**
 * Wrapper class for FML progress bar that is shown when Minecraft boots.
 *
 * @author ExE Boss
 */
public class FMLProgressBar implements nova.core.util.ProgressBar {

	private final ProgressBar progressBar;

	public FMLProgressBar(ProgressBar progressBar) {
		this.progressBar = progressBar;
	}

	@Override
	public void step(String s) {
		if (this.progressBar.getStep() >= this.progressBar.getSteps()) return;
		this.progressBar.step(s);
	}
}
