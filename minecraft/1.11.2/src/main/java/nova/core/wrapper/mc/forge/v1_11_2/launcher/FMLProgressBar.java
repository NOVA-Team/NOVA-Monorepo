/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nova.core.wrapper.mc.forge.v1_11_2.launcher;

import net.minecraftforge.fml.common.ProgressManager.ProgressBar;
import nova.core.util.AbstractProgressBar;

/**
 * Wrapper class for FML progress bar that is shown when Minecraft boots.
 *
 * @author ExE Boss
 */
public class FMLProgressBar extends AbstractProgressBar {

	private final ProgressBar progressBar;

	public FMLProgressBar(ProgressBar progressBar) {
		this.progressBar = progressBar;
	}

	@Override
	public void stepImpl(String s) {
		if (this.progressBar.getStep() >= this.progressBar.getSteps()) return;
		this.progressBar.step(s);
	}

	@Override
	protected void finishImpl() {
		while (progressBar.getStep() < progressBar.getSteps())
			progressBar.step("");
	}
}
