package nova.core.render.pipeline;

import nova.core.component.transform.Orientation;

public class OrientationRenderStream extends RenderStream {
	public final Orientation orientation;

	public OrientationRenderStream(Orientation orientation) {
		this.orientation = orientation;
		consumer = model -> model.matrix.rotate(orientation.orientation().rotation);
	}
}
