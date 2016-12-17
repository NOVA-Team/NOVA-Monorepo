package nova.core.block.component;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import nova.core.component.Component;
import nova.core.sound.Sound;

import java.util.Optional;

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
	 * The standard, regular block hardness is 1. {@code Double.POSITIVE_INFINITY} is unbreakable.
	 * </p>
	 */
	public static class Hardness extends Component implements BlockProperty {
		private double hardness = 1.0;

		/**
		 * Sets the breaking difficulty.
		 *
		 * @param hardness The breaking difficulty.
		 * @return This instance for chaining if desired.
		 */
		public Hardness setHardness(double hardness) {
			this.hardness = hardness;
			return this;
		}

		/**
		 * Gets the breaking difficulty.
		 *
		 * @return The breaking difficulty.
		 */
		public double getHardness() {
			return hardness;
		}
	}

	/**
	 * The blast resistance of a block.
	 * <p>
	 * The standard, regular block resistance is 1. {@link Double#POSITIVE_INFINITY} is unexplodable.
	 * </p>
	 */
	public static class Resistance extends Component implements BlockProperty {
		private double resistance = 1.0;

		/**
		 * Sets the blast resistance
		 *
		 * @param resistance The blast resistance.
		 * @return This instance for chaining if desired.
		 */
		public Resistance setResistance(double resistance) {
			this.resistance = resistance;
			return this;
		}

		/**
		 * Gets the blast resistance.
		 *
		 * @return The blast resistance.
		 */
		public double getResistance() {
			return resistance;
		}
	}

	/**
	 * The block sounds associated with a block.
	 *
	 * @author winsock
	 */
	public static class BlockSound extends Component implements BlockProperty {
		public BiMap<BlockSoundTrigger, Sound> blockSoundSoundMap = HashBiMap.create();
		public Optional<BiMap<String, Sound>> customDefinedSounds;

		/**
		 * Sets a sound to play on a specified trigger. Note to set a {@link BlockSoundTrigger#CUSTOM_TRIGGER} use {@link BlockSound#setCustomBlockSound(String,Sound)}
		 *
		 * @param trigger The trigger to set the sound for
		 * @param sound The sound to play on the triggering of the trigger
		 * @return This instance for chaining if desired.
		 */
		public BlockSound setBlockSound(BlockSoundTrigger trigger, Sound sound) {
			if (trigger == BlockSoundTrigger.CUSTOM_TRIGGER) {
				return this;
			}
			blockSoundSoundMap.put(trigger, sound);
			return this;
		}

		/**
		 * Sets a sound to an id of choice
		 *
		 * @param id The custom id for the sound
		 * @param sound The sound to associate with the id
		 * @return This instance for chaining if desired.
		 */
		public BlockSound setCustomBlockSound(String id, Sound sound) {
			if (!customDefinedSounds.isPresent()) {
				customDefinedSounds = Optional.of(HashBiMap.create());
			}
			customDefinedSounds.get().put(id, sound);
			return this;
		}

		/**
		 * Get the sound associated with a trigger
		 *
		 * @param trigger The trigger to get the sound for
		 * @return The sound object associated with the trigger
		 */
		public Sound getSound(BlockSoundTrigger trigger) {
			if (trigger == BlockSoundTrigger.CUSTOM_TRIGGER) {
				return null;
			}
			if (blockSoundSoundMap.containsKey(trigger)) {
				return blockSoundSoundMap.get(trigger);
			}
			return null;
		}

		/**
		 * Get the sound associated with a custom Id
		 *
		 * @param customId The custom id of the sound
		 * @return The sound object associated with the custom Id
		 */
		public Sound getCustomSound(String customId) {
			if (!customDefinedSounds.isPresent()) {
				return null;
			}
			if (customDefinedSounds.get().containsKey(customId)) {
				return customDefinedSounds.get().get(customId);
			}
			return null;
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
	public static class Opacity extends Component implements BlockProperty {
		// TODO maybe consider a double for opacity? Where 0 is 100% transparent and 1 is 100% opaque?

		/**
		 * This boolean determines if the block should allow light through itself or not.
		 */
		public boolean allowsLightThrough = false;

		/**
		 * Sets that the block should allow light through
		 *
		 * @return This instance for chaining if desired.
		 */
		public Opacity setTransparent() {
			allowsLightThrough = true;
			return this;
		}

		/**
		 * Sets if light should be transmitted through this block
		 *
		 * @param allowLightThrough Boolean flag if light should be allowed through
		 * @return This instance for chaining if desired.
		 */
		public Opacity setLightTransmission(boolean allowLightThrough) {
			this.allowsLightThrough = allowLightThrough;
			return this;
		}
	}

	// TODO
//	/**
//	 * Indicates whether the block is replaceable.
//	 */
//	public static final class Replaceable extends Component implements BlockProperty {
//		/**
//		 * Singleton for Replaceable.
//		 */
//		private static Replaceable theInstance = null;
//
//		/**
//		 * Gets the singleton for Replaceable.
//		 *
//		 * @return The singleton for Replaceable.
//		 */
//		public static Replaceable instance() {
//			if (theInstance == null) {
//				theInstance = new Replaceable();
//			}
//
//			return theInstance;
//		}
//
//		private Replaceable() {
//		}
//
//		@Override
//		public boolean equals(Object o) {
//			return this == o || o instanceof Replaceable;
//		}
//
//		@Override
//		public int hashCode() {
//			return Replaceable.class.hashCode();
//		}
//	}
}
