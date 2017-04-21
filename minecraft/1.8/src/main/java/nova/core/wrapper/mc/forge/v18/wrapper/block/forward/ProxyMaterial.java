package nova.core.wrapper.mc.forge.v18.wrapper.block.forward;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import nova.core.block.component.BlockProperty;

import java.util.Optional;

/**
 * @author soniex2, ExE Boss
 */
public class ProxyMaterial extends Material {
	private final Optional<BlockProperty.Opacity> opacity;
	private final Optional<BlockProperty.Replaceable> replaceable;

	/**
	 * Construct a new proxy material.
	 * @param color The map color.
	 * @param opacity The Opacity to use.
	 */
	public ProxyMaterial(MapColor color, Optional<BlockProperty.Opacity> opacity, Optional<BlockProperty.Replaceable> replaceable) {
		super(color);
		this.opacity = opacity;
		this.replaceable = replaceable;
	}

	@Override
	public boolean blocksLight() {
		return opacity.isPresent() ? opacity.get().opacity == 1 : super.blocksLight();
	}

	@Override
	public boolean isOpaque() {
		return opacity.isPresent() ? opacity.get().opacity == 1 : super.isOpaque();
	}

	@Override
	public boolean isReplaceable() {
		return replaceable.isPresent() ? replaceable.get().replaceFilter.test(Optional.empty()) : super.isReplaceable();
	}
}
