/*
 * Copyright (c) 2015 NOVA, All rights reserved.
 * This library is free software, licensed under GNU Lesser General Public License version 3
 *
 * This file is part of NOVA.
 *
 * NOVA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NOVA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NOVA.  If not, see <http://www.gnu.org/licenses/>.
 */

package nova.core.util;

import nova.core.block.Block;
import nova.core.block.BlockFactory;
import nova.core.block.BlockManager;
import nova.core.component.misc.Collider;
import nova.core.entity.Entity;
import nova.core.entity.EntityFactory;
import nova.core.entity.EntityManager;
import nova.core.event.bus.GlobalEvents;
import nova.core.loader.Mod;
import nova.core.util.math.RotationUtil;
import nova.internal.core.launch.NovaLauncher;
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
		launcher = NovaLauncherTestFactory.createDummyLauncher(RayTraceMod.class);
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

		List<RayTracer.RayTraceBlockResult> rayTraceBlockResults1 = new RayTracer(Ray.fromInterval(Vector3D.ZERO, Direction.SOUTH.toVector().add(new Vector3D(0.1, 0.1, 0))))
			.setDistance(10)
			.rayTraceBlocks(fakeWorld)
			.collect(Collectors.toList());

		assertThat(rayTraceBlockResults1).hasSize(1);
		RayTracer.RayTraceBlockResult res = rayTraceBlockResults1.get(0);

		assertThat(res.side).isEqualTo(Direction.NORTH);
	}

	//TODO: Make ray trace entity unit test

	@Mod(id = "rayTrace", name = "ray", version = "1.0", novaVersion = "0.0.1")
	public static class RayTraceMod {
		public static BlockFactory solid;
		public static EntityFactory testEntity;

		public RayTraceMod(GlobalEvents events, BlockManager blockManager, EntityManager entityManager) {
			events.on(BlockManager.Init.class).bind(evt -> this.registerBlocks(evt.manager));
			events.on(EntityManager.Init.class).bind(evt -> this.registerEntities(evt.manager));
		}

		public void registerBlocks(BlockManager blockManager) {
			solid = blockManager.register(
				"solid",
				() -> {
					Block solid = new Block();
					solid.components.add(new Collider(solid));
					return solid;
				}
			);
		}

		private void registerEntities(EntityManager entityManager) {
			testEntity = entityManager.register("test", Entity::new);
		}
	}
}
