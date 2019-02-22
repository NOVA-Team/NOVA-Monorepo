package nova.core.wrapper.mc.forge.v1_11_2.wrapper.block.forward;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import nova.core.block.component.BlockProperty;

import java.util.Optional;

/**
 * @author soniex2
 */
public class ProxyMaterial extends Material {
	private final Optional<BlockProperty.Opacity> opacity;
	private final Optional<BlockProperty.Replaceable> replaceable;

	/**
	 * Construct a new proxy material.
	 * @param color The map color.
	 * @param opacity The Opacity to use.
	 * @param replaceable If this block is replaceable.
	 */
	public ProxyMaterial(MapColor color, Optional<BlockProperty.Opacity> opacity, Optional<BlockProperty.Replaceable> replaceable) {
		super(color);
		this.opacity = opacity;
		this.replaceable = replaceable;
	}

	@Override
	public boolean blocksLight() {
		return opacity.map(BlockProperty.Opacity::isOpaque).orElseGet(super::blocksLight);
	}

	@Override
	public boolean isOpaque() {
		return opacity.map(BlockProperty.Opacity::isOpaque).orElseGet(super::isOpaque);
	}

	@Override
	public boolean isReplaceable() {
		return replaceable.map(BlockProperty.Replaceable::isReplaceable).orElseGet(super::isReplaceable);
	}
}
