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
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Calclavia
 */
public class RayTraceTest {

	static NovaLauncher launcher;
	FakeWorld fakeWorld;

	@BeforeClass
	public static void init() {
		launcher = new NovaLauncherTestFactory(RayTraceMod.class).createLauncher();
	}

	@Before
	public void setup() {
		fakeWorld = new FakeWorld();
	}

	@Test
	public void testRayTraceBlock() {
		fakeWorld.setBlock(new Vector3i(5, 5, 5), RayTraceMod.solid);

		List<RayTracer.RayTraceBlockResult> rayTraceBlockResults1 =
			new RayTracer(new RayTracer.Ray(new Vector3d(5, 0, 5), new Vector3d(0, 1, 0)))
				.setDistance(6)
				.rayTraceBlocks(fakeWorld)
				.collect(Collectors.toList());

		assertThat(rayTraceBlockResults1).hasSize(1);
		assertThat(rayTraceBlockResults1.get(0).block.position()).isEqualTo(new Vector3i(5, 5, 5));

		fakeWorld.setBlock(new Vector3i(6, 5, 5), RayTraceMod.solid);
		List<RayTracer.RayTraceBlockResult> rayTraceBlockResults2 = new RayTracer(new RayTracer.Ray(new Vector3d(0, 5, 5), new Vector3d(1, 0, 0)))
			.setDistance(7)
			.rayTraceBlocks(fakeWorld)
			.collect(Collectors.toList());

		assertThat(rayTraceBlockResults2).hasSize(2);
		assertThat(rayTraceBlockResults2.get(0).block.position()).isEqualTo(new Vector3i(5, 5, 5));
	}

	@Test
	public void testRayTraceEfficiency() {
		int maxTestSize = 10000;

		Game.logger().info("Generating random world with block count: " + maxTestSize);

		/**
		 * Generate a random world
		 */
		Random random = new Random();

		IntStream.range(0, 500)
			.mapToObj(value -> Vector3d.random().multiply(random.nextInt(maxTestSize)).toInt())
			.forEach(pos -> fakeWorld.setBlock(pos, RayTraceMod.solid));

		Game.logger().info("World Generated");

		for (int size = 0; size <= maxTestSize; size += 200) {
			/**
			 * Do random ray trace
			 */
			RayTracer rayTracer = new RayTracer(new RayTracer.Ray(new Vector3d(0, 5, 5), new Vector3d(1, 0, 0))).setDistance(size);
			Game.logger().info("Ray tracing with threading: " + rayTracer.doParallel());
			Profiler start = new Profiler("Ray Trace " + size).start();
			rayTracer.rayTraceBlocks(fakeWorld).findFirst();
			start.end();
			//assertThat(elapsed).isLessThan(0.1);
		}
	}

	@Test
	public void testRayTraceEntity() {
		fakeWorld.setBlock(new Vector3i(5, 5, 5), RayTraceMod.solid);

		Entity entity = fakeWorld.addEntity(RayTraceMod.testEntity);
		entity.setPosition(new Vector3d(5, 0, 5));
		entity.setRotation(Quaternion.fromEuler(0, Math.PI / 2, 0));

		RayTracer rayTracer = new RayTracer(entity).setDistance(10);
		List<RayTracer.RayTraceBlockResult> rayTraceBlockResults = rayTracer.rayTraceBlocks(fakeWorld).collect(Collectors.toList());
		assertThat(rayTraceBlockResults.size()).isEqualTo(1);
	}

	//TODO: Make ray trace entity unit test

	@NovaMod(id = "rayTrace", name = "ray", version = "1.0", novaVersion = "0.0.1")
	public static class RayTraceMod implements Loadable {
		public static BlockFactory solid;
		public static EntityFactory testEntity;

		@Override
		public void preInit() {
			solid = Game.blocks().register(args -> {
				FakeBlock solid = new FakeBlock("solid");
				solid.add(new Collider());
				return solid;
			});

			testEntity = Game.entities().register(objects -> new Entity() {
				@Override
				public String getID() {
					return "test";
				}
			});
		}
	}
}
