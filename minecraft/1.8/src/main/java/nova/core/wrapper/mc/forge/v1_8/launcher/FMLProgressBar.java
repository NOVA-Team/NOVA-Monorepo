/*
 * Copyright (c) 2017 NOVA, All rights reserved.
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

package nova.core.wrapper.mc.forge.v1_8.launcher;

import nova.core.util.AbstractProgressBar;

/**
 * Wrapper class for FML progress bar that is shown when Minecraft boots.
 *
 * @author ExE Boss
 */
public class FMLProgressBar extends AbstractProgressBar {

	@SuppressWarnings("deprecation")
	private final net.minecraftforge.fml.common.ProgressManager.ProgressBar progressBar;

	public FMLProgressBar(@SuppressWarnings("deprecation")
		net.minecraftforge.fml.common.ProgressManager.ProgressBar progressBar) {
		this.progressBar = progressBar;
	}

	@Override
	protected void stepImpl(String s) {
		if (this.progressBar.getStep() >= this.progressBar.getSteps()) return;
		this.progressBar.step(s);
	}

	@Override
	protected void finishImpl() {
		super.finish();
		while (progressBar.getStep() < progressBar.getSteps())
			progressBar.step("");
	}
}
