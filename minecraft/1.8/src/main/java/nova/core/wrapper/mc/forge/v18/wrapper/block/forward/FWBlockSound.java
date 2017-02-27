package nova.core.wrapper.mc.forge.v18.wrapper.block.forward;

import net.minecraft.block.Block;
import nova.core.block.component.BlockProperty;

/**
 * @author winsock, soniex2, ExE Boss
 */
public class FWBlockSound extends Block.SoundType {
	private final BlockProperty.BlockSound blockSound;

	/**
	 * Construct a new FWBlockSound
	 * @param blockSound The BlockSound to use.
	 */
	public FWBlockSound(BlockProperty.BlockSound blockSound) {
		super("", 1f, 1f);
		this.blockSound = blockSound;
	}

	@Override
	public String getBreakSound() {
		return blockSound.getSound(BlockProperty.BlockSound.BlockSoundTrigger.BREAK)
			.map(sound -> (sound.domain.isEmpty() && !sound.name.contains(".")) ? "dig." + sound.name : sound.getID())
			.orElseGet(super::getBreakSound);
	}

	@Override
	public String getStepSound() {
		return blockSound.getSound(BlockProperty.BlockSound.BlockSoundTrigger.WALK)
			.map(sound -> (sound.domain.isEmpty() && !sound.name.contains(".")) ? "step." + sound.name : sound.getID())
			.orElseGet(super::getStepSound);
	}

	@Override
	public String getPlaceSound() {
		return blockSound.getSound(BlockProperty.BlockSound.BlockSoundTrigger.PLACE)
			.map(sound -> sound.domain.isEmpty() ? sound.name : sound.getID())
			.orElseGet(this::getBreakSound); // By default MC uses the block break sound for block placement
	}
}
