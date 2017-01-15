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
package nova.core.util;

/**
 * @author ExE Boss
 */
public abstract class AbstractProgressBar implements ProgressBar {
	private boolean finished = false;

	@Override
	public final void step(String message) {
		if (isFinished())
			throw new IllegalStateException("ProgressBar is finished.");

		stepImpl(message);
	}

	/**
	 * Advance one step. This method is called by {@link AbstractProgressBar#step(java.lang.String)}.
	 *
	 * @param message The message to show for the current step.
	 */
	protected abstract void stepImpl(String message);

	@Override
	public final void finish() {
		if (this.finished)
			return;

		this.finished = true;

		finishImpl();
	}

	/**
	 * This method is called by {@link AbstractProgressBar#finish()}.
	 *
	 * Here you do anything that should be done when the progress bar is finished.
	 */
	protected void finishImpl() {}

	@Override
	public final boolean isFinished() {
		return finished;
	}
}
