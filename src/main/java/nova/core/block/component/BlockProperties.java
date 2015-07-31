package nova.core.block.component;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import nova.core.component.Component;
import nova.core.sound.Sound;

import java.util.Optional;

/**
 * This optional component is for properties that alter a blocks function
 * These properties are intended to effect all blocks of one type.
 * TODO: Make a component for blocks that have instance properties. E.G. In Minecraft NBT data
 * - Note(winsock): I think instance block data storage is an agnostic voxel game feature that goes beyond just 'dumb' blocks
 * Created by winsock on 7/2/15.
 */
public class BlockProperties extends Component {

	/**
	 * Triggers for sounds to play on the location of a block
	 */
	public enum BlockSoundTrigger {
		BREAK,
		PLACE,
		WALK,
		CUSTOM_TRIGGER
	}

	public BiMap<BlockSoundTrigger, Sound> blockSoundSoundMap = HashBiMap.create();
	public Optional<BiMap<String, Sound>> customDefinedSounds;

	/**
	 * This boolean determines if the block should allow light through itself or not.
	 */
	public boolean allowsLightThrough = false;

	/**
	 * Sets a sound to play on a specified trigger. Note to set a @see{BlockSoundTrigger.CUSTOM_TRIGGER} use @see{BlockProperties.setCustomBlockSound(String,Sound)}
	 *
	 * @param trigger The trigger to set the sound for
	 * @param sound The sound to play on the triggering of the trigger
	 * @return This instance for chaining if desired.
	 */
	public BlockProperties setBlockSound(BlockSoundTrigger trigger, Sound sound) {
		if (trigger == BlockSoundTrigger.CUSTOM_TRIGGER) {
			return this;
		}
		blockSoundSoundMap.put(trigger, sound);
		return this;
	}

	public BlockProperties setBlockSound(BlockSoundTrigger trigger, String soundResourceString) {
		return setBlockSound(trigger, new Sound("", soundResourceString));
	}

	/**
	 * Sets a sound to an id of choice
	 *
	 * @param id The custom id for the sound
	 * @param sound The sound to associate with the id
	 * @return This instance for chaining if desired.
	 */
	public BlockProperties setCustomBlockSound(String id, Sound sound) {
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
	 * Sets that the block should allow light through
	 *
	 * @return This instance for chaining if desired.
	 */
	public BlockProperties setTransparent() {
		allowsLightThrough = true;
		return this;
	}

	/**
	 * Sets if light should be transmitted through this block
	 *
	 * @param allowLightThrough Boolean flag if light should be allowed through
	 * @return This instance for chaining if desired.
	 */
	public BlockProperties setLightTransmission(boolean allowLightThrough) {
		this.allowsLightThrough = allowLightThrough;
		return this;
	}
}
