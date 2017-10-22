/*
 * Copyright (c) 2016 NOVA, All rights reserved.
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

package nova.core.block.component;

import nova.core.component.Component;
import nova.core.component.SidedComponent;
import nova.core.component.UnsidedComponent;
import nova.core.sound.Sound;
import nova.core.util.math.MathUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

/**
 * Block properties.
 *
 * @author soniex2
 */
public interface BlockProperty {
	/**
	 * The breaking difficulty of a block, or how long it takes to break a block.
	 * Tools and armour may make the block break faster or slower than this.
	 * <p>
	 * The standard, regular block hardness is 1. {@link Double#POSITIVE_INFINITY} is unbreakable.
	 * </p>
	 */
	@UnsidedComponent
	public static class Hardness extends Component implements BlockProperty {
		private DoubleSupplier hardness = () -> 1;

		/**
		 * Sets the breaking difficulty.
		 *
		 * @param hardness The breaking difficulty.
		 * @return This instance for chaining if desired.
		 */
		public Hardness setHardness(DoubleSupplier hardness) {
			this.hardness = hardness;
			return this;
		}

		/**
		 * Sets the breaking difficulty.
		 *
		 * @param hardness The breaking difficulty.
		 * @return This instance for chaining if desired.
		 */
		public Hardness setHardness(double hardness) {
			return this.setHardness(() -> hardness);
		}

		/**
		 * Gets the breaking difficulty.
		 *
		 * @return The breaking difficulty.
		 */
		public double getHardness() {
			return hardness.getAsDouble();
		}
	}

	/**
	 * The blast resistance of a block, indicates how many cubic meters of TNT are needed to explode it.
	 * <p>
	 * The standard, regular block resistance is 1. {@link Double#POSITIVE_INFINITY} is unexplodable.
	 * </p>
	 */
	@UnsidedComponent
	public static class Resistance extends Component implements BlockProperty {
		private DoubleSupplier resistance = () -> 1;

		/**
		 * Sets the blast resistance, indicates how many cubic meters
		 * of TNT are needed to explode it.
		 *
		 * @param resistance The blast resistance.
		 * @return This instance for chaining if desired.
		 */
		public Resistance setResistance(DoubleSupplier resistance) {
			this.resistance = resistance;
			return this;
		}

		/**
		 * Sets the blast resistance, indicates how many cubic meters
		 * of TNT are needed to explode it.
		 *
		 * @param resistance The blast resistance.
		 * @return This instance for chaining if desired.
		 */
		public Resistance setResistance(double resistance) {
			return this.setResistance(() -> resistance);
		}

		/**
		 * Gets the blast resistance, indicates how many cubic meters
		 * of TNT are needed to explode it.
		 *
		 * @return The blast resistance.
		 */
		public double getResistance() {
			return resistance.getAsDouble();
		}
	}

	/**
	 * The block sounds associated with a block.
	 *
	 * @author winsock
	 */
	@UnsidedComponent
	public static class BlockSound extends Component implements BlockProperty {
		private final Map<BlockSoundTrigger, Sound> blockSounds = new HashMap<>();
		private final Map<String, Sound> customSounds = new HashMap<>();

		/**
		 * Sets a sound to play on a specified trigger. Note to set a {@link BlockSoundTrigger#CUSTOM_TRIGGER} use {@link BlockSound#setCustomBlockSound(String,Sound)}
		 *
		 * @param trigger The trigger to set the sound for
		 * @param sound The sound to play on the triggering of the trigger
		 * @return This instance for chaining if desired.
		 */
		public BlockSound setBlockSound(BlockSoundTrigger trigger, Sound sound) {
			if (trigger != BlockSoundTrigger.CUSTOM_TRIGGER)
				blockSounds.put(trigger, sound);
			return this;
		}

		/**
		 * Sets a sound to an id of choice
		 *
		 * @param customTrigger The custom id for the sound
		 * @param sound The sound to associate with the id
		 * @return This instance for chaining if desired.
		 */
		public BlockSound setCustomBlockSound(String customTrigger, Sound sound) {
			customSounds.put(customTrigger, sound);
			return this;
		}

		/**
		 * Get the sound associated with a trigger
		 *
		 * @param trigger The trigger to get the sound for
		 * @return The sound object associated with the trigger
		 */
		public Optional<Sound> getSound(BlockSoundTrigger trigger) {
			if (trigger == BlockSoundTrigger.CUSTOM_TRIGGER)
				return Optional.empty();
			return Optional.ofNullable(blockSounds.get(trigger));
		}

		/**
		 * Get the sound associated with a custom Id
		 *
		 * @param customTrigger The custom id of the sound
		 * @return The sound object associated with the custom Id
		 */
		public Optional<Sound> getCustomSound(String customTrigger) {
			return Optional.ofNullable(customSounds.get(customTrigger));
		}

		/**
		 * Triggers for sounds to play on the location of a block
		 */
		public enum BlockSoundTrigger {
			BREAK,
			PLACE,
			WALK,
			CUSTOM_TRIGGER
		}
	}

	/**
	 * The opacity associated with a block.
	 *
	 * @author winsock
	 */
	@SidedComponent
	@SuppressWarnings("deprecation")
	public static class Opacity extends Component implements BlockProperty {
		private static final DoubleSupplier TRANSPARENT = () -> 0;
		private static final DoubleSupplier OPAQUE = () -> 1;

		/**
		 * This value determines if the block should allow light through itself or not.
		 */
		private DoubleSupplier opacity = OPAQUE;

		/**
		 * Sets that the block should allow light through
		 *
		 * @return This instance for chaining if desired.
		 */
		public Opacity setTransparent() {
			opacity = TRANSPARENT;
			return this;
		}

		/**
		 * Sets that the block should disallow light through
		 *
		 * @return This instance for chaining if desired.
		 */
		public Opacity setOpaque() {
			opacity = OPAQUE;
			return this;
		}

		/**
		 * Sets if light should be transmitted through this block
		 *
		 * @param opacity The block's opacity
		 * @return This instance for chaining if desired.
		 */
		public Opacity setOpacity(DoubleSupplier opacity) {
			this.opacity = opacity;
			return this;
		}

		/**
		 * Sets if light should be transmitted through this block
		 *
		 * @param opacity The block's opacity
		 * @return This instance for chaining if desired.
		 */
		public Opacity setOpacity(double opacity) {
			return this.setOpacity(opacity <= 0 ? TRANSPARENT : (opacity >= 1 ? OPAQUE : () -> opacity));
		}

		/**
		 * This value determines if the block should allow light through itself or not.
		 *
		 * @return The block's opacity
		 */
		public double getOpacity() {
			return MathUtil.clamp(opacity.getAsDouble(), 0, 1);
		}

		/**
		 * Checks if the block should allow light through
		 *
		 * @return If the block should allow light through
		 */
		public boolean isTransparent() {
			return getOpacity() < 1;
		}

		/**
		 * Checks if the block should disallow light through
		 *
		 * @return If the block should disallow light through
		 */
		public boolean isOpaque() {
			return getOpacity() == 1;
		}
	}

	/**
	 * Indicates whether the block is replaceable.
	 *
	 * @author ExE Boss
	 */
	@UnsidedComponent
	public static class Replaceable extends Component implements BlockProperty {
		private BooleanSupplier replaceable = () -> true;

		public Replaceable() {}

		/**
		 * Set the boolean supplier that is used to check if this block is replaceable.
		 *
		 * @param replaceable The replacement boolean supplier.
		 * @return This instance for chaining if desired.
		 */
		public Replaceable setReplaceable(BooleanSupplier replaceable) {
			this.replaceable = replaceable;
			return this;
		}

		/**
		 * Check if this block can be replaced.
		 *
		 * @return if this block can be replaced.
		 */
		public boolean isReplaceable() {
			return this.replaceable.getAsBoolean();
		}
	}
}
