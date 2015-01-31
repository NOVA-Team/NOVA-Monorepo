package nova.wrapper.mc1710.util;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.TextureStitchEvent;
import nova.core.game.Game;
import nova.core.render.Texture;

import java.util.HashMap;

/**
 * @author Calclavia
 */
@SideOnly(Side.CLIENT)
public class RenderUtility {

	public static final RenderUtility instance = new RenderUtility();
	private final HashMap<Texture, IIcon> iconMap = new HashMap<>();

	public IIcon getIcon(Texture texture) {
		return iconMap.get(texture);
	}

	/**
	 * Handles NOVA texture registration.
	 */
	@SubscribeEvent
	public void preTextureHook(TextureStitchEvent.Pre event) {
		if (event.map.getTextureType() == 0) {
			Game.instance.get().renderManager.blockTextures.forEach(t -> registerIcon(t, event));
		} else if (event.map.getTextureType() == 1) {

		}
	}

	public void registerIcon(Texture texture, TextureStitchEvent.Pre event) {
		iconMap.put(texture, event.map.registerIcon(texture.resource));
	}

	@SubscribeEvent
	public void textureHook(TextureStitchEvent.Post event) {

	}
}
