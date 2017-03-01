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

package nova.core.sound;

import nova.core.util.Asset;
import nova.core.util.Identifiable;

/**
 * An object representing a sound. (including the modification to pitch and volume, etc...)
 *
 * @author skyem123
 */
public class Sound extends Asset implements Identifiable {

	/**
	 * Changes the pitch of the sound.
	 * This does not change the speed of the sound so it can it be used to compensate for the speed of the sound changing to pitch.
	 */
	public final float pitch;
	/**
	 * Changes the speed of the sound.
	 * This also changes the pitch of the sound, and so the pitch change should be compensated with pitchModification.
	 */
	public final float speed;
	/**
	 * Changes the volume of the sound.
	 * Default is 1 for no modification.
	 */
	public final float volume;

	public Sound(String domain, String name) {
		this(domain, name, 1, 1, 1);
	}

	public Sound(String domain, String name, float pitch, float speed, float volume) {
		super(domain, name);
		this.pitch = pitch;
		this.speed = speed;
		this.volume = volume;
	}

	public Sound withPitch(float pitch) {
		return new Sound(domain, name, pitch, speed, volume);
	}

	public Sound withSpeed(float speed) {
		return new Sound(domain, name, pitch, speed, volume);
	}

	public Sound withVolume(float volume) {
		return new Sound(domain, name, pitch, speed, volume);
	}

	public Sound withPS(float pitch, float speed) {
		return new Sound(domain, name, pitch, speed, volume);
	}

	public Sound withPV(float pitch, float volume) {
		return new Sound(domain, name, pitch, speed, volume);
	}

	public Sound withSV(float speed, float volume) {
		return new Sound(domain, name, pitch, speed, volume);
	}

	public Sound with(float pitch, float speed, float volume) {
		return new Sound(domain, name, pitch, speed, volume);
	}
}
