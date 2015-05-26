package nova.core.render.texture;

/**
 * A texture that will be stiched into a large Atlass Texture
 *
 * @author Calclavia
 */
public class BlockTexture extends Texture {

	public BlockTexture(String domain, String resource) {
		super(domain, resource);
	}

	@Override
	public String getPath() {
		return "textures/blocks/" + super.getPath();
	}
}
