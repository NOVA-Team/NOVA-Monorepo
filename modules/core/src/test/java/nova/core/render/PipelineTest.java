package nova.core.render;

import nova.core.render.model.MeshModel;
import nova.core.render.model.Model;
import nova.core.render.pipeline.RenderPipeline;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import static nova.testutils.NovaAssertions.assertThat;

/**
 * Tests the render pipeline
 * @author Calclavia
 */
public class PipelineTest {

	@Test
	public void testRenderStream1() throws Exception {
		Model model = new MeshModel();

		RenderPipeline renderPipeline1 = new RenderPipeline().apply(new RenderPipeline(m -> m.addChild(new MeshModel())));

		renderPipeline1.build().accept(model);

		assertThat(model.children).hasSize(1);
	}

	@Test
	public void testRenderStream2() throws Exception {
		Model model = new MeshModel();
		RenderPipeline renderPipeline1 = new RenderPipeline().apply(new RenderPipeline(m -> m.addChild(new MeshModel())));
		RenderPipeline renderPipeline = renderPipeline1.apply(new RenderPipeline(m -> m.addChild(new MeshModel())));

		renderPipeline.build().accept(model);

		Assertions.assertThat(model.children).hasSize(2);
	}
}