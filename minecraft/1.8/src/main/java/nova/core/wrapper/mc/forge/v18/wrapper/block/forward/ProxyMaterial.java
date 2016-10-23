package nova.core.wrapper.mc.forge.v18.wrapper.block.forward;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import nova.core.block.component.BlockProperty;
import nova.core.block.component.BlockProperty.Opacity;

/**
 * @author soniex2
 */
public class ProxyMaterial extends Material {
	private final Opacity opacity;

	/**
	 * Construct a new proxy material.
	 * @param color The map color.
	 * @param opacity The Opacity to use.
	 */
	public ProxyMaterial(MapColor color, Opacity opacity) {
		super(color);
		this.opacity = opacity;
	}

	@Override
	public boolean blocksLight() {
		return opacity.allowsLightThrough;
	}

	@Override
	public boolean isOpaque() {
		return opacity.allowsLightThrough;
	}

}
