package nova.core.render.pipeline;

import nova.core.render.model.Model;

import java.util.function.Consumer;

/**
 * A render transmutation transforms a {@link RenderStream}.
 *
 * @author Calclavia
 */
public abstract class RenderTransmutation implements Consumer<RenderStream> {

	protected RenderStream renderStream;

	@Override
	public void accept(RenderStream renderStream) {
		this.renderStream = renderStream;
	}

	public RenderStream stream() {
		return renderStream;
	}

	public Consumer<Model> build() {
		return renderStream.result;
	}
}
