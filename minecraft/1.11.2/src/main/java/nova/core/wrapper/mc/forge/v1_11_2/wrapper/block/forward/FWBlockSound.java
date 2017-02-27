package nova.core.wrapper.mc.forge.v1_11_2.wrapper.block.forward;

import net.minecraft.block.SoundType;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import nova.core.block.component.BlockProperty;

/**
 * @author winsock, soniex2, ExE Boss
 */
public class FWBlockSound extends SoundType {
	private final BlockProperty.BlockSound blockSound;

	/**
	 * Construct a new FWBlockSound
	 * @param blockSound The BlockSound to use.
	 */
	public FWBlockSound(BlockProperty.BlockSound blockSound) {
		super(1f, 1f, SoundEvents.BLOCK_STONE_BREAK, SoundEvents.BLOCK_STONE_STEP, SoundEvents.BLOCK_STONE_PLACE, SoundEvents.BLOCK_STONE_HIT, SoundEvents.BLOCK_STONE_FALL);
		this.blockSound = blockSound;
	}

	@Override
	public SoundEvent getBreakSound() {
		return blockSound.getSound(BlockProperty.BlockSound.BlockSoundTrigger.BREAK)
			.map(sound -> (sound.domain.isEmpty() && !sound.name.contains(".")) ? "dig." + sound.name : sound.getID())
			.map(soundID -> SoundEvent.REGISTRY.getObject(new ResourceLocation(soundID)))
			.orElseGet(super::getBreakSound);
	}

	@Override
	public SoundEvent getStepSound() {
		return blockSound.getSound(BlockProperty.BlockSound.BlockSoundTrigger.WALK)
			.map(sound -> (sound.domain.isEmpty() && !sound.name.contains(".")) ? "step." + sound.name : sound.getID())
			.map(soundID -> SoundEvent.REGISTRY.getObject(new ResourceLocation(soundID)))
			.orElseGet(super::getBreakSound);
	}

	@Override
	public SoundEvent getPlaceSound() {
		return blockSound.getSound(BlockProperty.BlockSound.BlockSoundTrigger.PLACE)
			.map(sound -> sound.domain.isEmpty() ? "step." + sound.name : sound.getID())
			.map(soundID -> SoundEvent.REGISTRY.getObject(new ResourceLocation(soundID)))
			.orElseGet(super::getPlaceSound);
	}
}
