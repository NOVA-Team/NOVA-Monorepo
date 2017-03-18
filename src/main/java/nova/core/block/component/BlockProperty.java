package nova.core.block.component;

import nova.core.block.BlockFactory;
import nova.core.component.Component;
import nova.core.component.SidedComponent;
import nova.core.component.UnsidedComponent;
import nova.core.sound.Sound;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

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
	@UnsidedComponent
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
	@UnsidedComponent
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
	public static class Opacity extends Component implements BlockProperty {
		/**
		 * This value determines if the block should allow light through itself or not.
		 */
		public double opacity = 1;

		/**
		 * Sets that the block should allow light through
		 *
		 * @return This instance for chaining if desired.
		 */
		public Opacity setTransparent() {
			opacity = 0;
			return this;
		}

		/**
		 * Sets if light should be transmitted through this block
		 *
		 * @param opacity The block's opacity
		 * @return This instance for chaining if desired.
		 */
		public Opacity setOpacity(double opacity) {
			this.opacity = opacity;
			return this;
		}
	}

	/**
	 * Indicates whether the block is replaceable.
	 */
	@UnsidedComponent
	public static class Replaceable extends Component implements BlockProperty {
		/**
		 * The replacement filter. An empty optional means that it is impossible
		 * to determine the factory of the block to replace this block with.
		 */
		public Predicate<Optional<BlockFactory>> replaceFilter = block -> true;

		public Replaceable() {
		}

		public Replaceable setReplaceFilter(Predicate<Optional<BlockFactory>> replaceFilter) {
			this.replaceFilter = replaceFilter;
			return this;
		}

		@Override
		public boolean equals(Object o) {
			return this == o || o instanceof Replaceable;
		}

		@Override
		public int hashCode() {
			return Replaceable.class.hashCode();
		}
	}
}
