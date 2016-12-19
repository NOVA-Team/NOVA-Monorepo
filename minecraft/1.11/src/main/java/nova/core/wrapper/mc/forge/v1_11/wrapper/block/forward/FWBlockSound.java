package nova.core.wrapper.mc.forge.v1_11.wrapper.block.forward;

import net.minecraft.block.SoundType;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import nova.core.block.component.BlockProperty.BlockSound;
import nova.core.sound.Sound;

/**
 * @author winsock, soniex2
 */
public class FWBlockSound extends SoundType {
	private final BlockSound blockSound;

	/**
	 * Construct a new FWBlockSound
	 * @param blockSound The BlockSound to use.
	 */
	public FWBlockSound(BlockSound blockSound) {
		super(1f, 1f, SoundEvents.BLOCK_STONE_BREAK, SoundEvents.BLOCK_STONE_STEP, SoundEvents.BLOCK_STONE_PLACE, SoundEvents.BLOCK_STONE_HIT, SoundEvents.BLOCK_STONE_FALL);
		this.blockSound = blockSound;
	}

	@Override
	public SoundEvent getBreakSound() {
		if (blockSound.blockSoundSoundMap.containsKey(BlockSound.BlockSoundTrigger.BREAK)) {
			Sound sound = blockSound.blockSoundSoundMap.get(BlockSound.BlockSoundTrigger.BREAK);
			if (sound.domain.isEmpty() && !sound.name.contains(".")) {
				SoundEvent event = SoundEvent.REGISTRY.getObject(new ResourceLocation("dig." + sound.name));
				return event == null ? super.getPlaceSound() : event;
			}
			SoundEvent event = SoundEvent.REGISTRY.getObject(new ResourceLocation(sound.getID().asString())); // TODO?
			return event == null ? super.getPlaceSound() : event;
		}
		return super.getBreakSound();
	}

	@Override
	public SoundEvent getStepSound() {
		if (blockSound.blockSoundSoundMap.containsKey(BlockSound.BlockSoundTrigger.WALK)) {
			Sound sound = blockSound.blockSoundSoundMap.get(BlockSound.BlockSoundTrigger.WALK);
			if (sound.domain.isEmpty() && !sound.name.contains(".")) {
				SoundEvent event = SoundEvent.REGISTRY.getObject(new ResourceLocation("step." + sound.name));
				return event == null ? super.getPlaceSound() : event;
			}
			SoundEvent event = SoundEvent.REGISTRY.getObject(new ResourceLocation(sound.getID().asString())); // TODO?
			return event == null ? super.getPlaceSound() : event;
		}
		return super.getStepSound();
	}

	@Override
	public SoundEvent getPlaceSound() {
		if (blockSound.blockSoundSoundMap.containsKey(BlockSound.BlockSoundTrigger.WALK)) {
			Sound sound = blockSound.blockSoundSoundMap.get(BlockSound.BlockSoundTrigger.WALK);
			if (sound.domain.isEmpty()) {
				SoundEvent event = SoundEvent.REGISTRY.getObject(new ResourceLocation(sound.name));
				return event == null ? super.getPlaceSound() : event;
			}
			SoundEvent event = SoundEvent.REGISTRY.getObject(new ResourceLocation(sound.getID().asString())); // TODO?
			return event == null ? super.getPlaceSound() : event;
		}
		// By default MC uses the block break sound for block placement
		return super.getPlaceSound();
	}
}
