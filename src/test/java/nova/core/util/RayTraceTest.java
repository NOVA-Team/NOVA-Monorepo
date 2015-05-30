package nova.core.util;

import nova.core.block.BlockFactory;
import nova.core.component.misc.Collider;
import nova.core.entity.Entity;
import nova.core.entity.EntityFactory;
import nova.core.game.Game;
import nova.core.loader.Loadable;
import nova.core.loader.NovaMod;
import nova.core.util.transform.matrix.Quaternion;
import nova.core.util.transform.vector.Vector3d;
import nova.core.util.transform.vector.Vector3i;
import nova.internal.launch.NovaLauncher;
import nova.testutils.FakeBlock;
import nova.testutils.FakeWorld;
import nova.wrappertests.NovaLauncherTestFactory;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Calclavia
 */
public class RayTraceTest {

	@Test
	public void testRayTrace() {
		NovaLauncher launcher = new NovaLauncherTestFactory(RayTraceMod.class).createLauncher();

		FakeWorld fakeWorld = new FakeWorld();

		fakeWorld.setBlock(new Vector3i(5, 5, 5), RayTraceMod.solid);
		List<RayTracer.RayTraceBlockResult> rayTraceBlockResults1 = RayTracer.rayTraceBlock(fakeWorld, new Vector3d(5, 0, 5), new Vector3d(0, 1, 0), 6);
		assertThat(rayTraceBlockResults1).hasSize(1);
		assertThat(rayTraceBlockResults1.get(0).block.position()).isEqualTo(new Vector3i(5, 5, 5));

		fakeWorld.setBlock(new Vector3i(6, 5, 5), RayTraceMod.solid);
		List<RayTracer.RayTraceBlockResult> rayTraceBlockResults2 = RayTracer.rayTraceBlock(fakeWorld, new Vector3d(0, 5, 5), new Vector3d(1, 0, 0), 7);
		assertThat(rayTraceBlockResults2).hasSize(2);
		assertThat(rayTraceBlockResults2.get(0).block.position()).isEqualTo(new Vector3i(5, 5, 5));
	}

	@Test
	public void testRayTraceEntity() {
		NovaLauncher launcher = new NovaLauncherTestFactory(RayTraceMod.class).createLauncher();

		FakeWorld fakeWorld = new FakeWorld();

		fakeWorld.setBlock(new Vector3i(5, 5, 5), RayTraceMod.solid);

		Entity entity = fakeWorld.addEntity(RayTraceMod.testEntity);
		entity.setPosition(new Vector3d(5, 0, 5));
		entity.setRotation(Quaternion.fromEuler(0, Math.PI / 2, 0));

		List<RayTracer.RayTraceBlockResult> rayTraceBlockResults = RayTracer.rayTraceBlock(entity, 10);
		assertThat(rayTraceBlockResults.size()).isEqualTo(1);
	}

	@NovaMod(id = "rayTrace", name = "ray", version = "1.0", novaVersion = "0.0.1")
	public static class RayTraceMod implements Loadable {
		public static BlockFactory solid;
		public static EntityFactory testEntity;

		@Override
		public void preInit() {
			solid = Game.instance.blockManager.register(args -> {
				FakeBlock solid = new FakeBlock("solid");
				solid.add(new Collider());
				return solid;
			});

			testEntity = Game.instance.entityManager.register(objects -> new Entity() {
				@Override
				public String getID() {
					return "test";
				}
			});
		}
	}
}
