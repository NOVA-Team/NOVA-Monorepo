package nova.core.util;

import nova.core.block.Block;
import nova.core.block.BlockManager;
import nova.core.component.ComponentManager;
import nova.core.component.misc.Collider;
import nova.core.entity.Entity;
import nova.core.entity.EntityManager;
import nova.core.loader.Loadable;
import nova.core.loader.NovaMod;
import nova.core.util.math.RotationUtil;
import nova.core.util.math.Vector3DUtil;
import nova.internal.core.Game;
import nova.internal.core.launch.NovaLauncher;
import nova.testutils.FakeBlock;
import nova.testutils.FakeWorld;
import nova.wrappertests.NovaLauncherTestFactory;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static nova.testutils.NovaAssertions.assertThat;

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
		fakeWorld.setBlock(new Vector3D(5, 5, 5), RayTraceMod.solid);

		List<RayTracer.RayTraceBlockResult> rayTraceBlockResults1 =
			new RayTracer(new Ray(new Vector3D(5.5, 0, 5.5), new Vector3D(0, 1, 0)))
				.setDistance(6)
				.rayTraceBlocks(fakeWorld)
				.collect(Collectors.toList());

		assertThat(rayTraceBlockResults1).hasSize(1);
		assertThat(rayTraceBlockResults1.get(0).block.position()).isEqualTo(new Vector3D(5, 5, 5));

		fakeWorld.setBlock(new Vector3D(6, 5, 5), RayTraceMod.solid);
		List<RayTracer.RayTraceBlockResult> rayTraceBlockResults2 = new RayTracer(new Ray(new Vector3D(0, 5.5, 5.5), new Vector3D(1, 0, 0)))
			.setDistance(7)
			.rayTraceBlocks(fakeWorld)
			.collect(Collectors.toList());

		assertThat(rayTraceBlockResults2).hasSize(2);
		assertThat(rayTraceBlockResults2.get(0).block.position()).isEqualTo(new Vector3D(5, 5, 5));
	}

	@Test
	public void testRayTraceTunnel() {
		Vector3D start = new Vector3D(2, 7, 3);

		//Create a tunnel
		for (int i = 0; i < 5; i++)
			for (int d = 0; d < 4; d++)
				fakeWorld.setBlock(start.add(Direction.fromOrdinal(d).toVector()), RayTraceMod.solid);

		fakeWorld.setBlock(new Vector3D(5, 7, 3), RayTraceMod.solid);

		List<RayTracer.RayTraceBlockResult> rayTraceBlockResults1 =
			new RayTracer(new Ray(new Vector3D(0, 7.5, 3.5), new Vector3D(1, 0, 0)))
				.setDistance(10)
				.rayTraceBlocks(fakeWorld)
				.collect(Collectors.toList());

		assertThat(rayTraceBlockResults1).hasSize(1);
		RayTracer.RayTraceBlockResult rayTraceBlockResult = rayTraceBlockResults1.get(0);
		assertThat(rayTraceBlockResult.block.position()).isEqualTo(new Vector3D(5, 7, 3));
		assertThat(rayTraceBlockResult.hit).isEqualTo(new Vector3D(5, 7.5, 3.5));
	}

	@Test
	public void testRayTraceEntity() {
		fakeWorld.setBlock(new Vector3D(5, 5, 5), RayTraceMod.solid);

		Entity entity = fakeWorld.addEntity(RayTraceMod.testEntity);
		entity.setPosition(new Vector3D(5.1, 0, 5.1));
		entity.setRotation(new Rotation(RotationUtil.DEFAULT_ORDER, 0, Math.PI / 2, 0));

		RayTracer rayTracer = new RayTracer(entity).setDistance(10);
		List<RayTracer.RayTraceBlockResult> rayTraceBlockResults = rayTracer.rayTraceBlocks(fakeWorld).collect(Collectors.toList());
		assertThat(rayTraceBlockResults.size()).isEqualTo(1);
	}

	@Test
	public void testEdge() {
		fakeWorld.setBlock(new Vector3D(0, 0, 2), RayTraceMod.solid);

		List<RayTracer.RayTraceBlockResult> rayTraceBlockResults1 = new RayTracer(Ray.fromInterval(Vector3D.ZERO, Direction.SOUTH.toVector().add(new Vector3D(0.1,0.1,0))))
			.setDistance(10)
			.rayTraceBlocks(fakeWorld)
			.collect(Collectors.toList());

		assertThat(rayTraceBlockResults1).hasSize(1);
		RayTracer.RayTraceBlockResult res = rayTraceBlockResults1.get(0);

		assertThat(res.side).isEqualTo(Direction.NORTH);
	}

	//TODO: Make ray trace entity unit test

	@NovaMod(id = "rayTrace", name = "ray", version = "1.0", novaVersion = "0.0.1")
	public static class RayTraceMod implements Loadable {
		public static Factory<? extends Block> solid;
		public static Factory<? extends Entity> testEntity;

		final BlockManager blockManager;
		final EntityManager entityManager;
		final ComponentManager componentManager;

		public RayTraceMod(BlockManager blockManager, EntityManager entityManager, ComponentManager componentManager) {

			this.blockManager = blockManager;
			this.entityManager = entityManager;
			this.componentManager = componentManager;
		}

		@Override
		public void preInit() {
			blockManager.register(FakeBlock.class);
			solid = blockManager.getFactory("solid").get();
			componentManager.register(Collider.class);
			testEntity = entityManager.register(Factory.of(Entity.class).ID("TestEntity"));
		}
	}
}
