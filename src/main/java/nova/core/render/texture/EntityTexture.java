package nova.core.render.texture;

public class EntityTexture extends Texture {

	public EntityTexture(String domain, String resource) {
		super(domain, resource);
	}

	@Override
	public String getPath() {
		return "textures/entity/" + super.getPath();
	}
}
