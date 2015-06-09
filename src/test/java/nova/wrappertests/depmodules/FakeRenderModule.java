package nova.wrappertests.depmodules;

import nova.core.render.RenderManager;
import nova.core.render.texture.Texture;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import se.jbee.inject.bind.BinderModule;

/**
 * @author Calclavia
 */
public class FakeRenderModule extends BinderModule {

	@Override
	protected void declare() {
		bind(RenderManager.class).to(FakeRenderManager.class);
	}

	public static class FakeRenderManager extends RenderManager {
		@Override
		public Vector2D getDimension(Texture texture) {
			return Vector2D.ZERO;
		}
	}

}

