package nova.core.wrapper.mc.forge.v18.wrapper.block.forward;

import net.minecraft.block.Block;
import nova.core.block.component.BlockProperty.BlockSound;
import nova.core.sound.Sound;

/**
 * @author winsock, soniex2
 */
public class FWBlockSound extends Block.SoundType {
	private final BlockSound blockSound;

	/**
	 * Construct a new FWBlockSound
	 * @param blockSound The BlockSound to use.
	 */
	public FWBlockSound(BlockSound blockSound) {
		super("", 1f, 1f);
		this.blockSound = blockSound;
	}

	@Override
	public String getBreakSound() {
		if (blockSound.blockSoundSoundMap.containsKey(BlockSound.BlockSoundTrigger.BREAK)) {
			Sound sound = blockSound.blockSoundSoundMap.get(BlockSound.BlockSoundTrigger.BREAK);
			if (sound.domain.isEmpty() && !sound.name.contains(".")) {
				return "dig." + sound.name;
			}
			return sound.getID();
		}
		return super.getBreakSound();
	}

	@Override
	public String getStepSound() {
		if (blockSound.blockSoundSoundMap.containsKey(BlockSound.BlockSoundTrigger.WALK)) {
			Sound sound = blockSound.blockSoundSoundMap.get(BlockSound.BlockSoundTrigger.WALK);
			if (sound.domain.isEmpty() && !sound.name.contains(".")) {
				return "step." + sound.name;
			}
			return sound.getID();
		}
		return super.getStepSound();
	}

	@Override
	public String getPlaceSound() {
		if (blockSound.blockSoundSoundMap.containsKey(BlockSound.BlockSoundTrigger.WALK)) {
			Sound sound = blockSound.blockSoundSoundMap.get(BlockSound.BlockSoundTrigger.WALK);
			if (sound.domain.isEmpty()) {
				return sound.name;
			}
			return sound.getID();
		}
		// By default MC uses the block break sound for block placement
		return this.getBreakSound();
	}
}
