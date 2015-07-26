package nova.wrapper.mc18.wrapper.block.forward;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import nova.core.block.component.BlockProperties;
import nova.core.sound.ResourceSound;
import nova.core.sound.Sound;

/**
 * Created by winsock on 7/2/15.
 */
public class MCBlockProperties extends BlockProperties {
	private Material mcMaterial = null;

	public MCBlockProperties() {}

	public MCBlockProperties(BlockProperties copy) {
		this.blockSoundSoundMap = copy.blockSoundSoundMap;
		this.customDefinedSounds = copy.customDefinedSounds;
		this.allowsLightThrough = copy.allowsLightThrough;
	}

	public Material toMcMaterial() {
		if (mcMaterial == null) {
			// TODO: We most likely should offer a way to set the color.
			mcMaterial = new ProxyMaterial(MapColor.grayColor);
		}
		return mcMaterial;
	}

	public class ProxyMaterial extends Material {
		private final FWBlockSound sound = new FWBlockSound();

		public ProxyMaterial(MapColor color) {
			super(color);
		}

		@Override
		public boolean blocksLight() {
			return !MCBlockProperties.this.allowsLightThrough;
		}

		@Override
		public boolean isOpaque() {
			return !MCBlockProperties.this.allowsLightThrough;
		}

		public Block.SoundType getSoundType() {
			return sound;
		}

		private class FWBlockSound extends Block.SoundType {
			public FWBlockSound() {
				super("", 1f, 1f);
			}

			@Override
			public String getBreakSound() {
				if (MCBlockProperties.this.blockSoundSoundMap.containsKey(BlockSoundTrigger.BREAK)) {
					Sound sound = MCBlockProperties.this.blockSoundSoundMap.get(BlockSoundTrigger.BREAK);
					if (sound instanceof ResourceSound) {
						if (((ResourceSound) sound).domain.isEmpty() && !((ResourceSound) sound).resource.contains(".")) {
							return "dig." + ((ResourceSound) sound).resource;
						}
						return sound.getID();
					}
				}
				return super.getBreakSound();
			}

			@Override
			public String getStepSound() {
				if (MCBlockProperties.this.blockSoundSoundMap.containsKey(BlockSoundTrigger.WALK)) {
					Sound sound = MCBlockProperties.this.blockSoundSoundMap.get(BlockSoundTrigger.WALK);
					if (sound instanceof ResourceSound) {
						if (((ResourceSound) sound).domain.isEmpty() && !((ResourceSound) sound).resource.contains(".")) {
							return "step." + ((ResourceSound) sound).resource;
						}
						return sound.getID();
					}
				}
				return super.getStepSound();
			}

			@Override
			public String getPlaceSound() {
				if (MCBlockProperties.this.blockSoundSoundMap.containsKey(BlockSoundTrigger.WALK)) {
					Sound sound = MCBlockProperties.this.blockSoundSoundMap.get(BlockSoundTrigger.WALK);
					if (sound instanceof ResourceSound) {
						if (((ResourceSound) sound).domain.isEmpty()) {
							return ((ResourceSound) sound).resource;
						}
						return sound.getID();
					}
				}
				// By default MC uses the block break sound for block placement
				return this.getBreakSound();
			}
		}
	}
}
